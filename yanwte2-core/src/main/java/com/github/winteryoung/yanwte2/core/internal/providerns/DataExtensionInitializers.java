package com.github.winteryoung.yanwte2.core.internal.providerns;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.winteryoung.yanwte2.core.DataExtInitializer;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.reflect.ClassPath;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public class DataExtensionInitializers {
    private DataExtensionInitializers() {}

    private static final Function<Object, Object> NULL_INITIALIZER = (a) -> null;

    /**
     * Two level map. Host extensible object -> provider namespace -> data extension initializer.
     */
    private static Cache<Object, Map<String, Function<Object, Object>>> cache =
            CacheBuilder.newBuilder().weakKeys().build();

    private static Multimap<String, Function<Object, Object>> providerNsToInitializersMap =
            HashMultimap.create();

    private static Map<Function<Object, Object>, Class<?>> initializerToHostExtensibleObjectMap =
            Maps.newHashMap();

    private static void put(
            Object hostExtensibleObject,
            String providerNamespace,
            Function<Object, Object> initializer) {
        checkNotNull(hostExtensibleObject);
        checkNotNull(providerNamespace);
        checkNotNull(initializer);

        try {
            Map<String, Function<Object, Object>> secLevelMap =
                    cache.get(hostExtensibleObject, ConcurrentHashMap::new);
            secLevelMap.put(providerNamespace, initializer);
        } catch (UncheckedExecutionException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static Function<Object, Object> get(
            Object hostExtensibleObject, String providerNamespace) {
        checkNotNull(hostExtensibleObject);
        checkNotNull(providerNamespace);

        Function<Object, Object> initializer = getOrNull(hostExtensibleObject, providerNamespace);
        if (initializer == null) {
            Function<Object, Object> _initializer =
                    initInitializer(hostExtensibleObject, providerNamespace);
            if (_initializer != null) {
                return _initializer;
            }
        }

        return initializer;
    }

    private static synchronized Function<Object, Object> initInitializer(
            Object hostExtensibleObject, String providerNamespace) {
        Collection<Function<Object, Object>> initializers =
                providerNsToInitializersMap.get(providerNamespace);

        if (initializers.isEmpty()) {
            initializers = initProviderNs(providerNamespace);
            providerNsToInitializersMap.putAll(providerNamespace, initializers);

            for (Function<Object, Object> initializer : initializers) {
                initializerToHostExtensibleObjectMap.put(
                        initializer, hostExtensibleObject.getClass());
            }
        }

        for (Function<Object, Object> initializer : initializers) {
            Class<?> hostExtensibleClass = initializerToHostExtensibleObjectMap.get(initializer);
            if (hostExtensibleClass == hostExtensibleObject.getClass()) {
                put(hostExtensibleObject, providerNamespace, initializer);
                return initializer;
            }
        }

        put(hostExtensibleObject, providerNamespace, NULL_INITIALIZER);
        return NULL_INITIALIZER;
    }

    @SuppressWarnings("unchecked")
    private static Collection<Function<Object, Object>> initProviderNs(String providerNamespace) {
        try {
            List result =
                    ClassPath.from(Thread.currentThread().getContextClassLoader())
                            .getTopLevelClasses(providerNamespace)
                            .stream()
                            .map(ClassPath.ClassInfo::load)
                            .filter(DataExtInitializer.class::isAssignableFrom)
                            .map(
                                    klass -> {
                                        try {
                                            return klass.newInstance();
                                        } catch (IllegalAccessException
                                                | InstantiationException e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                            .collect(Collectors.toList());

            return (Collection<Function<Object, Object>>) result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Function<Object, Object> getOrNull(
            Object hostExtensibleObject, String providerNamespace) {
        try {
            Map<String, Function<Object, Object>> secLevelMap =
                    cache.get(hostExtensibleObject, ConcurrentHashMap::new);
            return secLevelMap.get(providerNamespace);
        } catch (UncheckedExecutionException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
