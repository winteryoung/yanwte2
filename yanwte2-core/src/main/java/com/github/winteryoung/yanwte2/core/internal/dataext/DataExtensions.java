package com.github.winteryoung.yanwte2.core.internal.dataext;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class DataExtensions {
    private DataExtensions() {}

    private static final Object NULL_DATA_EXTENSION = new Object();

    /** Two level map. Host extensible object -> provider namespace -> data extension. */
    private static Cache<Object, ConcurrentHashMap<String, Object>> cache =
            CacheBuilder.newBuilder().weakKeys().build();

    public static void put(Object extensibleData, String providerPackage, Object dataExtension) {
        checkNotNull(extensibleData);
        checkNotNull(providerPackage);

        try {
            ConcurrentHashMap<String, Object> secLevelMap =
                    cache.get(extensibleData, ConcurrentHashMap::new);

            if (dataExtension != null) {
                secLevelMap.put(providerPackage, dataExtension);
            } else {
                secLevelMap.put(providerPackage, NULL_DATA_EXTENSION);
            }
        } catch (UncheckedExecutionException | ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }
    }

    public static Object get(Object extensibleData, String providerPackage) {
        checkNotNull(extensibleData);
        checkNotNull(providerPackage);

        try {
            ConcurrentHashMap<String, Object> secLevelMap =
                    cache.get(extensibleData, ConcurrentHashMap::new);
            Object dataExtension = secLevelMap.get(providerPackage);

            if (dataExtension != null) {
                if (dataExtension != NULL_DATA_EXTENSION) {
                    return dataExtension;
                } else {
                    return null;
                }
            }

            Function<Object, Object> initializer =
                    DataExtensionInitializers.get(extensibleData, providerPackage);
            if (initializer != null) {
                dataExtension = initializer.apply(extensibleData);
                put(extensibleData, providerPackage, dataExtension);

                return get(extensibleData, providerPackage);
            }

            put(extensibleData, providerPackage, NULL_DATA_EXTENSION);
            return null;
        } catch (UncheckedExecutionException | ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }
    }
}
