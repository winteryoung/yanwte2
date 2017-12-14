package com.github.winteryoung.yanwte2.core.internal.providerns;

import static com.google.common.base.Preconditions.checkNotNull;

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

    public static void put(
            Object hostExtensibleObject, String providerNamespace, Object dataExtension) {
        checkNotNull(hostExtensibleObject);
        checkNotNull(providerNamespace);

        try {
            ConcurrentHashMap<String, Object> secLevelMap =
                    cache.get(hostExtensibleObject, ConcurrentHashMap::new);

            if (dataExtension != null) {
                secLevelMap.put(providerNamespace, dataExtension);
            } else {
                secLevelMap.put(providerNamespace, NULL_DATA_EXTENSION);
            }
        } catch (UncheckedExecutionException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object get(Object hostExtensibleObject, String providerNamespace) {
        checkNotNull(hostExtensibleObject);
        checkNotNull(providerNamespace);

        try {
            ConcurrentHashMap<String, Object> secLevelMap =
                    cache.get(hostExtensibleObject, ConcurrentHashMap::new);
            Object dataExtension = secLevelMap.get(providerNamespace);

            if (dataExtension != null) {
                if (dataExtension != NULL_DATA_EXTENSION) {
                    return dataExtension;
                } else {
                    return null;
                }
            }

            Function<Object, Object> initializer =
                    DataExtensionInitializers.get(hostExtensibleObject, providerNamespace);
            if (initializer != null) {
                dataExtension = initializer.apply(hostExtensibleObject);
                put(hostExtensibleObject, providerNamespace, dataExtension);

                return get(hostExtensibleObject, providerNamespace);
            }

            put(hostExtensibleObject, providerNamespace, NULL_DATA_EXTENSION);
            return null;
        } catch (UncheckedExecutionException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
