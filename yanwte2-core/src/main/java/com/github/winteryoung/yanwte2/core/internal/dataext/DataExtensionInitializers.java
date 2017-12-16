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
import java.util.stream.Collectors;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public class DataExtensionInitializers {
    private DataExtensionInitializers() {}

    private static final DataExtensionInitializer<Object, Object> NULL_INITIALIZER = (a) -> null;

    /**
     * Two level map. Host extensible object -> provider namespace -> data extension initializer.
     */
    private static Cache<Object, Map<String, DataExtensionInitializer<Object, Object>>> cache =
            CacheBuilder.newBuilder().weakKeys().build();

    private static Multimap<String, DataExtensionInitializer<Object, Object>>
            providerPackageToInitializersMap = HashMultimap.create();

    private static Map<DataExtensionInitializer<Object, Object>, Class<?>>
            initializerToExtensibleDataClassMap = Maps.newHashMap();

    private static void put(
            Object extensibleData,
            String providerPackage,
            DataExtensionInitializer<Object, Object> initializer) {
        checkNotNull(extensibleData);
        checkNotNull(providerPackage);
        checkNotNull(initializer);

        try {
            Map<String, DataExtensionInitializer<Object, Object>> secLevelMap =
                    cache.get(extensibleData, ConcurrentHashMap::new);
            secLevelMap.put(providerPackage, initializer);
        } catch (UncheckedExecutionException | ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }
    }

    public static DataExtensionInitializer<Object, Object> get(
            Object extensibleData, String providerPackage) {
        checkNotNull(extensibleData);
        checkNotNull(providerPackage);

        DataExtensionInitializer<Object, Object> initializer =
                getOrNull(extensibleData, providerPackage);
        if (initializer == null) {
            return initInitializer(extensibleData, providerPackage);
        }
        return initializer;
    }

    private static synchronized DataExtensionInitializer<Object, Object> initInitializer(
            Object extensibleData, String providerPackage) {
        Collection<DataExtensionInitializer<Object, Object>> initializers =
                providerPackageToInitializersMap.get(providerPackage);

        if (initializers.isEmpty()) {
            initializers = initProviderPackageInitializers(providerPackage);
            providerPackageToInitializersMap.putAll(providerPackage, initializers);

            for (DataExtensionInitializer<Object, Object> initializer : initializers) {
                initializerToExtensibleDataClassMap.put(initializer, extensibleData.getClass());
            }
        }

        for (DataExtensionInitializer<Object, Object> initializer : initializers) {
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
    private static Collection<DataExtensionInitializer<Object, Object>>
            initProviderPackageInitializers(String providerPackage) {
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

            return (Collection<DataExtensionInitializer<Object, Object>>) result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static DataExtensionInitializer<Object, Object> getOrNull(
            Object extensibleData, String providerPackage) {
        try {
            Map<String, DataExtensionInitializer<Object, Object>> secLevelMap =
                    cache.get(extensibleData, ConcurrentHashMap::new);
            return secLevelMap.get(providerPackage);
        } catch (UncheckedExecutionException | ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }
    }
}
