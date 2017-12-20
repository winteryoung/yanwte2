package com.github.winteryoung.yanwte2.core.internal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.internal.combinators.ProviderCombinator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.core.spi.SurrogateCombinator;
import com.github.winteryoung.yanwte2.core.utils.Lazy;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.*;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.UncheckedExecutionException;
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

    private static class ServiceTypeIndexEntry {
        Object proxy;
        ServiceOrchestrator orchestrator;
    }

    private static Cache<Class<?>, ServiceTypeIndexEntry> serviceTypeIndex =
            CacheBuilder.newBuilder().build();

    private static Cache<ClassLoader, Boolean> knownClassLoaders =
            CacheBuilder.newBuilder().weakKeys().build();

    public static <T extends Function> T getOrchestratorByServiceType(Class<T> serviceType) {
        checkNotNull(serviceType);
        checkArgument(
                serviceType.isInterface(),
                "Service type is required to be an interface: " + serviceType.getName());
        checkArgument(
                Function.class.isAssignableFrom(serviceType),
                "Service type is required to be a function: " + serviceType.getName());

        try {
            ServiceTypeIndexEntry entry =
                    serviceTypeIndex.get(
                            serviceType, () -> createServiceTypeIndexEntry(serviceType));
            //noinspection unchecked
            return (T) entry.proxy;
        } catch (UncheckedExecutionException | ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }
    }

    private static <T extends Function> ServiceTypeIndexEntry createServiceTypeIndexEntry(
            Class<T> serviceType) {
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
            T proxy = proxyType.newInstance();

            ServiceTypeIndexEntry entry = new ServiceTypeIndexEntry();
            entry.proxy = proxy;
            entry.orchestrator = userDefinedOrchestrator;

            return entry;
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
        Set<ProviderCombinator> providerCombinators = Sets.newHashSet();
        collectServiceProviderCombinators(node, providerCombinators);

        // make sure one package only contains one provider for a given service type
        Maps.uniqueIndex(providerCombinators, ProviderCombinator::getProviderPackage);
    }

    private static void collectServiceProviderCombinators(
            Combinator node, Set<ProviderCombinator> result) {
        //noinspection SuspiciousMethodCalls
        if (result.contains(node)) {
            return;
        }

        if (node instanceof ProviderCombinator) {
            result.add((ProviderCombinator) node);
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
                        new ByteBuddy().subclass(serviceType).make().load(classLoader).getLoaded();
                return (ServiceOrchestrator) implClass.newInstance();
            }

            loadYanwteOrchestrators(classLoader);

            ServiceTypeIndexEntry entry = serviceTypeIndex.getIfPresent(serviceType);
            if (entry != null) {
                return entry.orchestrator;
            }
            return null;
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    private static synchronized void loadYanwteOrchestrators(ClassLoader classLoader) {
        try {
            if (knownClassLoaders.getIfPresent(classLoader) != null) {
                return;
            } else {
                // to prevent some rotten class loaders from keeping loading resources,
                // we set the flag first
                knownClassLoaders.put(classLoader, true);
            }

            Enumeration<URL> resources = classLoader.getResources("META-INF/yanwte/orchestrators");
            ArrayList<URL> resourceList = Collections.list(resources);
            for (URL resource : resourceList) {
                Resources.asByteSource(resource)
                        .asCharSource(Charsets.UTF_8)
                        .lines()
                        .forEach(line -> loadOrchestrator(line.trim(), classLoader));
            }
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    private static void loadOrchestrator(String className, ClassLoader classLoader) {
        try {
            Class<?> klass = classLoader.loadClass(className);
            ServiceOrchestrator orchestrator = (ServiceOrchestrator) klass.newInstance();

            ServiceTypeIndexEntry entry = new ServiceTypeIndexEntry();
            entry.orchestrator = orchestrator;

            Class serviceType = orchestrator.getServiceType();
            serviceTypeIndex.put(serviceType, entry);
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }
}
