package com.github.winteryoung.yanwte2.core.internal.dataext;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.winteryoung.yanwte2.core.DataExtension;
import com.github.winteryoung.yanwte2.core.DataExtensionInitializer;
import com.github.winteryoung.yanwte2.core.ExtensibleData;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class DataExtensions {
    private DataExtensions() {}

    private static final Object NULL_DATA_EXTENSION = new Object();

    /** Two level map. Host extensible data -> provider namespace -> data extension. */
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

    public static Object get(
            ExtensibleData extensibleData, String providerPackage, String currentProviderPackage) {
        checkNotNull(extensibleData);
        checkNotNull(providerPackage);

        try {
            ConcurrentHashMap<String, Object> secLevelMap =
                    cache.get(extensibleData, ConcurrentHashMap::new);
            Object dataExtension = secLevelMap.get(providerPackage);

            if (dataExtension != null) {
                if (dataExtension instanceof DataExtension) {
                    DataExtension dataExt = (DataExtension) dataExtension;
                    Set<String> friendProviderPackages =
                            DataExtensionFriendPackagesCache.get(dataExt, providerPackage);

                    if (currentProviderPackage != null
                            && !friendProviderPackages.contains(currentProviderPackage)) {
                        throw new RuntimeException(
                                String.format(
                                        "Illegal access from provider package %s to data extension %s",
                                        currentProviderPackage, dataExt.getClass()));
                    }
                }
                if (dataExtension != NULL_DATA_EXTENSION) {
                    return dataExtension;
                } else {
                    return null;
                }
            }

            DataExtensionInitializer initializer =
                    DataExtensionInitializers.get(extensibleData, providerPackage);
            //noinspection unchecked
            dataExtension = initializer.createDataExtension(extensibleData);
            put(extensibleData, providerPackage, dataExtension);

            return get(extensibleData, providerPackage, currentProviderPackage);
        } catch (UncheckedExecutionException | ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }
    }
}
