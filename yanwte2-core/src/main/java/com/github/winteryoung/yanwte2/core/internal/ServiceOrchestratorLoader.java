package com.github.winteryoung.yanwte2.core.internal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import net.bytebuddy.ByteBuddy;
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

        try {
            orchestratorCacheByType.get(serviceType, () -> createOrchestratorByType(serviceType));
        } catch (ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }
    }

    private static <T> T createOrchestratorByType(Class<T> serviceType) {
        checkArgument(
                Function.class.isAssignableFrom(serviceType),
                "Service type is required to be a function: " + serviceType);

        new ByteBuddy()
                .subclass(AbstractGeneratedOrchestrator.class)
                .implement(serviceType)
                .method(ElementMatchers.named("apply"))
                .intercept();
    }
}
