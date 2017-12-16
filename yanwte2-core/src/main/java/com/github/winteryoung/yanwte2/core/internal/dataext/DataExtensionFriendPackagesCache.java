package com.github.winteryoung.yanwte2.core.internal.dataext;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.winteryoung.yanwte2.core.DataExtension;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class DataExtensionFriendPackagesCache {
    private DataExtensionFriendPackagesCache() {}

    private static Cache<Class<? extends DataExtension>, Set<String>> cache =
            CacheBuilder.newBuilder().build();

    public static Set<String> get(DataExtension dataExtension, String providerPackage) {
        checkNotNull(dataExtension);

        try {
            return cache.get(
                    dataExtension.getClass(),
                    () -> {
                        Set<String> friendProviderPackages =
                                dataExtension.getFriendProviderPackages();

                        if (friendProviderPackages == null) {
                            friendProviderPackages = Sets.newLinkedHashSet();
                        }

                        friendProviderPackages.add(providerPackage);

                        return friendProviderPackages;
                    });
        } catch (UncheckedExecutionException | ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }
    }
}
