package com.github.winteryoung.yanwte2.core.internal.dataext;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.winteryoung.yanwte2.core.DataExtensionInitializer;
import com.google.common.base.Throwables;
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

    private static Map<Function<Object, Object>, Class<?>> initializerToExtensibleDataClassMap =
            Maps.newHashMap();

    private static void put(
            Object extensibleData, String providerPackage, Function<Object, Object> initializer) {
        checkNotNull(extensibleData);
        checkNotNull(providerPackage);
        checkNotNull(initializer);

        try {
            Map<String, Function<Object, Object>> secLevelMap =
                    cache.get(extensibleData, ConcurrentHashMap::new);
            secLevelMap.put(providerPackage, initializer);
        } catch (UncheckedExecutionException | ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }
    }

    public static Function<Object, Object> get(Object extensibleData, String providerPackage) {
        checkNotNull(extensibleData);
        checkNotNull(providerPackage);

        Function<Object, Object> initializer = getOrNull(extensibleData, providerPackage);
        if (initializer == null) {
            return initInitializer(extensibleData, providerPackage);
        }
        return initializer;
    }

    private static synchronized Function<Object, Object> initInitializer(
            Object extensibleData, String providerPackage) {
        Collection<Function<Object, Object>> initializers =
                providerNsToInitializersMap.get(providerPackage);

        if (initializers.isEmpty()) {
            initializers = initProviderNs(providerPackage);
            providerNsToInitializersMap.putAll(providerPackage, initializers);

            for (Function<Object, Object> initializer : initializers) {
                initializerToExtensibleDataClassMap.put(initializer, extensibleData.getClass());
            }
        }

        for (Function<Object, Object> initializer : initializers) {
            Class<?> extensibleDataClass = initializerToExtensibleDataClassMap.get(initializer);
            if (extensibleDataClass == extensibleData.getClass()) {
                put(extensibleData, providerPackage, initializer);
                return initializer;
            }
        }

        put(extensibleData, providerPackage, NULL_INITIALIZER);
        return NULL_INITIALIZER;
    }

    @SuppressWarnings("unchecked")
    private static Collection<Function<Object, Object>> initProviderNs(String providerPackage) {
        try {
            List result =
                    ClassPath.from(Thread.currentThread().getContextClassLoader())
                            .getTopLevelClasses(providerPackage)
                            .stream()
                            .map(ClassPath.ClassInfo::load)
                            .filter(DataExtensionInitializer.class::isAssignableFrom)
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
            Object extensibleData, String providerPackage) {
        try {
            Map<String, Function<Object, Object>> secLevelMap =
                    cache.get(extensibleData, ConcurrentHashMap::new);
            return secLevelMap.get(providerPackage);
        } catch (UncheckedExecutionException | ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }
    }
}
