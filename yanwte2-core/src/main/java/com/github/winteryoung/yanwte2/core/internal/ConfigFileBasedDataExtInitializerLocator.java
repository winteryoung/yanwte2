package com.github.winteryoung.yanwte2.core.internal;

import static com.google.common.base.Preconditions.checkState;

import com.github.winteryoung.yanwte2.core.DataExtensionInitializer;
import com.github.winteryoung.yanwte2.core.ExtensibleData;
import com.github.winteryoung.yanwte2.core.spi.DataExtensionInitializerLocator;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fanshen
 * @since 2017/12/22
 */
public class ConfigFileBasedDataExtInitializerLocator implements DataExtensionInitializerLocator {
    @Override
    public Set<DataExtensionInitializer<ExtensibleData, ?>> getInitializers() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources("META-INF/yanwte/initializers");
            ArrayList<URL> resourceList = Collections.list(resources);
            ImmutableSet.Builder<DataExtensionInitializer<ExtensibleData, ?>> initializers =
                    ImmutableSet.builder();

            for (URL resource : resourceList) {
                Set<DataExtensionInitializer<ExtensibleData, ?>> _initializers =
                        Resources.asByteSource(resource)
                                .asCharSource(Charsets.UTF_8)
                                .lines()
                                .map(line -> loadInitializer(line.trim(), classLoader))
                                .collect(Collectors.toSet());

                initializers.addAll(_initializers);
            }

            return initializers.build();
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }

    private static synchronized DataExtensionInitializer<ExtensibleData, ?> loadInitializer(
            String className, ClassLoader classLoader) {
        @SuppressWarnings("unchecked")
        DataExtensionInitializer<ExtensibleData, ?> initializer =
                (DataExtensionInitializer) newInstance(className, classLoader);

        @SuppressWarnings("unchecked")
        Class<? extends ExtensibleData> extensibleDataType = initializer.getExtensibleDataType();

        checkState(
                extensibleDataType != ExtensibleData.class,
                "Generic types are required for " + className);

        return initializer;
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
