package com.github.winteryoung.yanwte2.core.internal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.core.spi.SurrogateCombinator;
import com.github.winteryoung.yanwte2.core.utils.Lazy;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
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

    private static Cache<Class<?>, Object> orchestratorCacheByType =
            CacheBuilder.newBuilder().build();

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
                    orchestratorCacheByType.get(
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
                                        createInterceptor(
                                                lazyTree, serviceType, userDefinedOrchestrator)))
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
            Lazy<Combinator> lazyTree,
            Class<? extends Function> serviceType,
            ServiceOrchestrator serviceOrchestrator) {
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

                            Set<String> providerPackages = collectNamedCombinators(tree);

                            expand(tree, providerPackages, serviceType);

                            return tree;
                        });

        return new OrchestratorInterceptor(
                (arg) -> {
                    Combinator combinator = lazyExpandedTree.get();
                    return combinator.invoke(arg);
                });
    }

    private static Set<String> collectNamedCombinators(Combinator tree) {
        return ImmutableSet.of();
    }

    private static void expand(
            Combinator combinator,
            Set<String> providerPackages,
            Class<? extends Function> serviceType) {
        checkNotNull(combinator);

        expandChildren(combinator, providerPackages, serviceType);

        for (Combinator child : combinator.getChildren()) {
            expand(child, providerPackages, serviceType);
        }
    }

    private static void expandChildren(
            Combinator combinator,
            Set<String> providerPackages,
            Class<? extends Function> serviceType) {
        List<Combinator> children = combinator.getChildren();
        checkNotNull(children);

        // check for multiple providers for one service type in the same package
        ServiceProviderLocators.locateAllProvidersIndexedByPackages(serviceType);

        children =
                children.stream()
                        .flatMap(
                                child -> {
                                    if (child instanceof SurrogateCombinator) {
                                        SurrogateCombinator surrogateCombinator =
                                                (SurrogateCombinator) child;
                                        return surrogateCombinator
                                                .getSurrogateCombinators(providerPackages)
                                                .stream();
                                    } else {
                                        return ImmutableList.of(child).stream();
                                    }
                                })
                        .collect(Collectors.toList());

        combinator.setChildren(children);
    }

    private static <T> ServiceOrchestrator loadUserDefinedOrchestrator(Class<T> serviceType) {
        ImmutableSet<ClassPath.ClassInfo> classInfos;
        try {
            classInfos =
                    ClassPath.from(serviceType.getClassLoader())
                            .getTopLevelClasses(serviceType.getPackage().getName());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        String serviceTypeName = serviceType.getName();

        List<String> expectedNames =
                Lists.newArrayList(serviceTypeName, serviceTypeName + "Orchestrator");

        for (ClassPath.ClassInfo classInfo : classInfos) {
            for (String expectedName : expectedNames) {
                if (expectedName.equals(classInfo.getName())) {
                    Class<?> orchestratorClass = classInfo.load();
                    if (ServiceOrchestrator.class.isAssignableFrom(orchestratorClass)) {
                        Class<?> orchestratorImplClass;
                        if (orchestratorClass.isInterface()) {
                            orchestratorImplClass =
                                    new ByteBuddy()
                                            .subclass(orchestratorClass)
                                            .make()
                                            .load(orchestratorClass.getClassLoader())
                                            .getLoaded();
                        } else {
                            orchestratorImplClass = orchestratorClass;
                        }

                        try {
                            return (ServiceOrchestrator) orchestratorImplClass.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

        return null;
    }
}
