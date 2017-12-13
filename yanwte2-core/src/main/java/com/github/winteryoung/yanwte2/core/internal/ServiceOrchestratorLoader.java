package com.github.winteryoung.yanwte2.core.internal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
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

    public static <T> T getOrchestratorByServiceType(Class<T> serviceType) {
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

    private static <T> T createOrchestratorByType(Class<T> serviceType) {
        ServiceOrchestrator userDefinedOrchestrator = loadUserDefinedOrchestrator(serviceType);
        checkState(
                userDefinedOrchestrator != null,
                "Cannot find orchestrator for service: " + serviceType.getName());

        LoadingCache<Integer, Combinator> lazyTree = getLazyCombinatorTree(userDefinedOrchestrator);

        Class<? extends T> proxyType =
                new ByteBuddy()
                        .subclass(serviceType)
                        .method(ElementMatchers.named("apply"))
                        .intercept(MethodDelegation.to(createInterceptor(lazyTree)))
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

    private static LoadingCache<Integer, Combinator> getLazyCombinatorTree(
            ServiceOrchestrator userDefinedOrchestrator) {
        return CacheBuilder.newBuilder()
                .build(
                        new CacheLoader<Integer, Combinator>() {
                            @Override
                            public Combinator load(
                                    @SuppressWarnings("NullableProblems") Integer key) {
                                return userDefinedOrchestrator.tree();
                            }
                        });
    }

    private static OrchestratorInterceptor createInterceptor(
            LoadingCache<Integer, Combinator> lazyTree) {
        return new OrchestratorInterceptor(
                (arg) -> {
                    Combinator combinator;
                    try {
                        combinator = lazyTree.get(1);
                    } catch (Exception e) {
                        Throwables.throwIfUnchecked(e);
                        throw new RuntimeException(e);
                    }
                    checkNotNull(combinator);
                    return combinator.invoke(arg);
                });
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
        String expectedName = serviceTypeName + "Orchestrator";

        for (ClassPath.ClassInfo classInfo : classInfos) {
            if (expectedName.equals(classInfo.getName())) {
                Class<?> orchestratorClass = classInfo.load();
                if (ServiceOrchestrator.class.isAssignableFrom(orchestratorClass)) {
                    try {
                        return (ServiceOrchestrator) orchestratorClass.newInstance();
                    } catch (Exception e) {
                        Throwables.throwIfUnchecked(e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return null;
    }
}
