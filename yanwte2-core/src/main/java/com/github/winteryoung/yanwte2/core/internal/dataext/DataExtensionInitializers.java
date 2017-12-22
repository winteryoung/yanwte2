package com.github.winteryoung.yanwte2.core.internal.dataext;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.github.winteryoung.yanwte2.core.DataExtensionInitializer;
import com.github.winteryoung.yanwte2.core.ExtensibleData;
import com.github.winteryoung.yanwte2.core.spi.DataExtensionInitializerLocator;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.*;
import java.util.*;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public class DataExtensionInitializers {
    private static class TypeIndexKey {
        TypeIndexKey(
                Class<? extends ExtensibleData> hostExtensibleDataType, String providerPackage) {
            this.hostExtensibleDataType = hostExtensibleDataType;
            this.providerPackage = providerPackage;
        }

        Class<? extends ExtensibleData> hostExtensibleDataType;
        String providerPackage;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TypeIndexKey that = (TypeIndexKey) o;
            return Objects.equals(hostExtensibleDataType, that.hostExtensibleDataType)
                    && Objects.equals(providerPackage, that.providerPackage);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hostExtensibleDataType, providerPackage);
        }

        @Override
        public String toString() {
            return "TypeIndexKey{"
                    + "hostExtensibleDataType="
                    + hostExtensibleDataType
                    + ", providerPackage='"
                    + providerPackage
                    + '\''
                    + '}';
        }
    }

    private DataExtensionInitializers() {}

    private static final DataExtensionInitializer<ExtensibleData, Object> NULL_INITIALIZER =
            (a) -> null;

    private static Cache<TypeIndexKey, DataExtensionInitializer<ExtensibleData, ?>> typeIndex =
            CacheBuilder.newBuilder().build();

    private static volatile boolean typeIndexInitialized = false;

    private static List<DataExtensionInitializerLocator> locators = Lists.newArrayList();

    static {
        for (DataExtensionInitializerLocator locator :
                ServiceLoader.load(DataExtensionInitializerLocator.class)) {
            locators.add(locator);
        }
    }

    public static DataExtensionInitializer<ExtensibleData, ?> get(
            ExtensibleData extensibleData, String providerPackage) {
        checkNotNull(extensibleData);
        checkNotNull(providerPackage);

        if (!typeIndexInitialized) {
            initTypeIndex();
        }

        DataExtensionInitializer<ExtensibleData, ?> initializer =
                typeIndex.getIfPresent(
                        new TypeIndexKey(extensibleData.getClass(), providerPackage));

        if (initializer == null) {
            return NULL_INITIALIZER;
        }
        return initializer;
    }

    private static synchronized void initTypeIndex() {
        for (DataExtensionInitializerLocator locator : locators) {
            Set<DataExtensionInitializer<ExtensibleData, ?>> initializers = locator.getInitializers();

            for (DataExtensionInitializer<ExtensibleData, ?> initializer : initializers) {
                indexInitializer(initializer);
            }
        }

        typeIndexInitialized = true;
    }

    private static synchronized void indexInitializer(
            DataExtensionInitializer<ExtensibleData, ?> initializer) {
        String className = initializer.getClass().getName();
        List<String> parts = Splitter.on(".").trimResults().splitToList(className);

        String providerPackage;
        if (parts.isEmpty()) {
            return;
        } else if (parts.size() == 1) {
            providerPackage = parts.get(0);
        } else {
            parts = parts.stream().limit(parts.size() - 1).collect(Collectors.toList());
            providerPackage = Joiner.on(".").join(parts);
        }

        @SuppressWarnings("unchecked")
        Class<? extends ExtensibleData> extensibleDataType = initializer.getExtensibleDataType();

        checkState(
                extensibleDataType != ExtensibleData.class,
                "Generic types are required for " + className);

        typeIndex.put(new TypeIndexKey(extensibleDataType, providerPackage), initializer);
    }
}
