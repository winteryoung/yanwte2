package com.github.winteryoung.yanwte2.core.internal.dataext;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.github.winteryoung.yanwte2.core.DataExtensionInitializer;
import com.github.winteryoung.yanwte2.core.ExtensibleData;
import com.google.common.base.*;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.*;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public class DataExtensionInitializers {
    private DataExtensionInitializers() {}

    private static final DataExtensionInitializer<? extends ExtensibleData, Object>
            NULL_INITIALIZER = (a) -> null;

    /**
     * Two level map. Host extensible object -> provider namespace -> data extension initializer.
     */
    private static Cache<Object, Map<String, DataExtensionInitializer>> mainCache =
            CacheBuilder.newBuilder().weakKeys().build();

    private static Multimap<String, DataExtensionInitializer> providerPackageIndex =
            HashMultimap.create();

    private static Map<Class<? extends DataExtensionInitializer>, Class<? extends ExtensibleData>>
            initializerTypeIndex = Maps.newHashMap();

    private static void put(
            Object extensibleData, String providerPackage, DataExtensionInitializer initializer) {
        checkNotNull(extensibleData);
        checkNotNull(providerPackage);
        checkNotNull(initializer);

        try {
            Map<String, DataExtensionInitializer> secLevelMap =
                    mainCache.get(extensibleData, ConcurrentHashMap::new);
            //noinspection unchecked
            secLevelMap.put(providerPackage, initializer);
        } catch (UncheckedExecutionException | ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }
    }

    public static DataExtensionInitializer get(
            ExtensibleData extensibleData, String providerPackage) {
        checkNotNull(extensibleData);
        checkNotNull(providerPackage);

        DataExtensionInitializer<? extends ExtensibleData, Object> initializer =
                getOrNull(extensibleData, providerPackage);
        if (initializer == null) {
            return initInitializer(extensibleData, providerPackage);
        }
        return initializer;
    }

    private static synchronized DataExtensionInitializer<?, ?> initInitializer(
            ExtensibleData extensibleData, String providerPackage) {
        Collection<DataExtensionInitializer> initializers =
                providerPackageIndex.get(providerPackage);

        if (initializers.isEmpty()) {
            loadInitializers();
            initializers = providerPackageIndex.get(providerPackage);
        }

        for (DataExtensionInitializer<?, ?> initializer : initializers) {
            Class<? extends ExtensibleData> extensibleDataType =
                    initializerTypeIndex.get(initializer.getClass());
            if (extensibleDataType == null) {
                return null;
            }

            if (extensibleDataType == extensibleData.getClass()) {
                put(extensibleData, providerPackage, initializer);
                return initializer;
            }
        }

        put(extensibleData, providerPackage, NULL_INITIALIZER);
        return NULL_INITIALIZER;
    }

    private static DataExtensionInitializer<? extends ExtensibleData, Object> getOrNull(
            Object extensibleData, String providerPackage) {
        try {
            Map<String, DataExtensionInitializer> secLevelMap =
                    mainCache.get(extensibleData, ConcurrentHashMap::new);
            //noinspection unchecked
            return secLevelMap.get(providerPackage);
        } catch (UncheckedExecutionException | ExecutionException e) {
            Throwables.throwIfUnchecked(e.getCause());
            throw new RuntimeException(e.getCause());
        }
    }

    private static void loadInitializers() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources("META-INF/yanwte/initializers");
            ArrayList<URL> resourceList = Collections.list(resources);
            for (URL resource : resourceList) {
                Resources.asByteSource(resource)
                        .asCharSource(Charsets.UTF_8)
                        .lines()
                        .forEach(line -> loadInitializer(line.trim(), classLoader));
            }
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    private static synchronized void loadInitializer(String className, ClassLoader classLoader) {
        List<String> parts = Splitter.on(".").trimResults().splitToList(className);

        String pkg;
        if (parts.isEmpty()) {
            return;
        } else if (parts.size() == 1) {
            pkg = parts.get(0);
        } else {
            parts = parts.stream().limit(parts.size() - 1).collect(Collectors.toList());
            pkg = Joiner.on(".").join(parts);
        }

        DataExtensionInitializer initializer =
                (DataExtensionInitializer) newInstance(className, classLoader);
        providerPackageIndex.put(pkg, initializer);

        @SuppressWarnings("unchecked")
        Class<? extends ExtensibleData> extensibleDataType = initializer.getExtensibleDataType();

        checkState(
                extensibleDataType != ExtensibleData.class,
                "Generic types are required for " + className);

        initializerTypeIndex.put(initializer.getClass(), extensibleDataType);
    }

    private static Object newInstance(String klass, ClassLoader classLoader) {
        try {
            return classLoader.loadClass(klass).newInstance();
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }
}
