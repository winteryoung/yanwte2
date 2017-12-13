package com.github.winteryoung.yanwte2.core.internal.providerns;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class DataExtensionCache {
    private DataExtensionCache() {}

    /** Two level map. Host extensible data -> provider namespace -> data extension. */
    private static Cache<Object, ConcurrentHashMap<String, Object>> cache =
            CacheBuilder.newBuilder().weakKeys().build();

    public static void put(
            Object hostExtensibleData, String providerNamespace, Object dataExtension) {
        checkNotNull(hostExtensibleData);
        checkNotNull(providerNamespace);

        try {
            ConcurrentHashMap<String, Object> secLevelMap =
                    cache.get(hostExtensibleData, ConcurrentHashMap::new);
            secLevelMap.put(providerNamespace, dataExtension);
        } catch (UncheckedExecutionException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object get(Object hostExtensibleData, String providerNamespace) {
        checkNotNull(hostExtensibleData);
        checkNotNull(providerNamespace);

        try {
            ConcurrentHashMap<String, Object> secLevelMap =
                    cache.get(hostExtensibleData, ConcurrentHashMap::new);
            return secLevelMap.get(providerNamespace);
        } catch (UncheckedExecutionException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
