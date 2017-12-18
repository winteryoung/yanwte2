package com.github.winteryoung.yanwte2.core.internal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.internal.combinators.ServiceProviderCombinator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.core.spi.SurrogateCombinator;
import com.github.winteryoung.yanwte2.core.utils.Lazy;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.*;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author Winter Young
 * @since 2017/12/10
 */
public class ServiceOrchestratorLoader {
    private ServiceOrchestratorLoader() {}

    private static Cache<Class<?>, Object> orchestratorsKeyedByServiceType =
            CacheBuilder.newBuilder().build();

    private static Cache<String, String> orchestratorsKeyedByServiceName =
            CacheBuilder.newBuilder().build();

    static {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources =
                    contextClassLoader.getResources("META-INF/yanwte-orchestrators");
            ArrayList<URL> resourceList = Collections.list(resources);
            for (URL resource : resourceList) {
                Resources.asByteSource(resource)
                        .asCharSource(Charsets.UTF_8)
                        .lines()
                        .forEach(
                                line -> {
                                    Iterable<String> splits = Splitter.on('=').limit(2).split(line);
                                    String[] kv = Iterables.toArray(splits, String.class);
                                    if (kv.length == 2) {
                                        orchestratorsKeyedByServiceName.put(kv[0], kv[1]);
                                    }
                                });
            }
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    public static <T extends Function> T getOrchestratorByServiceType(Class<T> serviceType) {
        checkNotNull(serviceType);
        checkArgument(
                serviceType.isInterface(),
                "Service type is required to be an interface: " + serviceType.getName());
        checkArgument(
                Function.class.isAssignableFrom(serviceType),
                "Service type is required to be a function: " + serviceType.getName());

        Object orchestrator;
        try {
            orchestrator =
                    orchestratorsKeyedByServiceType.get(
                            serviceType, () -> createOrchestratorByType(serviceType));
        } catch (UncheckedExecutionException | ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }

        //noinspection unchecked
        return (T) orchestrator;
    }

    private static <T extends Function> T createOrchestratorByType(Class<T> serviceType) {
        ServiceOrchestrator userDefinedOrchestrator = loadUserDefinedOrchestrator(serviceType);
        checkState(
                userDefinedOrchestrator != null,
                "Cannot find orchestrator for service: " + serviceType.getName());

        Lazy<Combinator> lazyTree = CombinatorTreeCache.getLazyTree(userDefinedOrchestrator);

        Class<? extends T> proxyType =
                new ByteBuddy()
                        .subclass(serviceType)
                        .method(ElementMatchers.named("apply"))
                        .intercept(
                                MethodDelegation.to(
                                        createInterceptor(lazyTree, userDefinedOrchestrator)))
                        .make()
                        .load(serviceType.getClassLoader())
                        .getLoaded();

        try {
            return proxyType.newInstance();
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    private static OrchestratorInterceptor createInterceptor(
            Lazy<Combinator> lazyTree, ServiceOrchestrator serviceOrchestrator) {
        Lazy<Combinator> lazyExpandedTree =
                Lazy.of(
                        () -> {
                            Combinator tree = lazyTree.get();

                            checkState(
                                    tree != null,
                                    "Combinator is required in tree() definition. "
                                            + "If you don't have one, use empty() instead");

                            checkState(
                                    !(tree instanceof SurrogateCombinator),
                                    "The root of tree cannot be a surrogate "
                                            + "combinator, orchestrator: "
                                            + serviceOrchestrator);

                            expand(tree);

                            validate(tree);

                            return tree;
                        });

        return new OrchestratorInterceptor(
                (arg) -> {
                    Combinator combinator = lazyExpandedTree.get();
                    return combinator.invoke(arg);
                });
    }

    private static void validate(Combinator node) {
        Set<ServiceProviderCombinator> providerCombinators = Sets.newHashSet();
        collectServiceProviderCombinators(node, providerCombinators);

        // make sure one package only contains one provider for a given service type
        Maps.uniqueIndex(providerCombinators, ServiceProviderCombinator::getProviderPackage);
    }

    private static void collectServiceProviderCombinators(
            Combinator node, Set<ServiceProviderCombinator> result) {
        //noinspection SuspiciousMethodCalls
        if (result.contains(node)) {
            return;
        }

        if (node instanceof ServiceProviderCombinator) {
            result.add((ServiceProviderCombinator) node);
        }

        List<Combinator> children = node.getChildren();
        if (children == null) {
            return;
        }

        for (Combinator child : children) {
            collectServiceProviderCombinators(child, result);
        }
    }

    private static void expand(Combinator combinator) {
        checkNotNull(combinator);

        expandChildren(combinator);

        for (Combinator child : combinator.getChildren()) {
            expand(child);
        }
    }

    private static void expandChildren(Combinator combinator) {
        List<Combinator> children = combinator.getChildren();
        checkNotNull(children);

        children =
                children.stream()
                        .flatMap(
                                child -> {
                                    if (child instanceof SurrogateCombinator) {
                                        SurrogateCombinator surrogateCombinator =
                                                (SurrogateCombinator) child;
                                        return surrogateCombinator
                                                .getSurrogateCombinators()
                                                .stream();
                                    } else {
                                        return ImmutableList.of(child).stream();
                                    }
                                })
                        .collect(Collectors.toList());

        combinator.setChildren(children);
    }

    private static <T> ServiceOrchestrator loadUserDefinedOrchestrator(Class<T> serviceType) {
        try {
            ClassLoader classLoader = serviceType.getClassLoader();

            if (ServiceOrchestrator.class.isAssignableFrom(serviceType)) {
                Class<? extends T> implClass =
                        new ByteBuddy()
                                .subclass(serviceType)
                                .make()
                                .load(classLoader)
                                .getLoaded();
                return (ServiceOrchestrator) implClass.newInstance();
            }

            String orchestratorName = orchestratorsKeyedByServiceName.getIfPresent(serviceType.getName());
            if (orchestratorName != null) {
                Class<?> klass = classLoader.loadClass(orchestratorName);
                return (ServiceOrchestrator) klass.newInstance();
            }

            return null;
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }
}
