package com.github.winteryoung.yanwte2.core;

import com.google.common.reflect.TypeToken;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public interface DataExtensionInitializer<ED extends ExtensibleData, DataExtension> {
    DataExtension createDataExtension(ED extensibleData);

    default Class<? extends ExtensibleData> getExtensibleDataType() {
        TypeToken<?> typeToken = new TypeToken<DataExtensionInitializer<ED, DataExtension>>() {};
        typeToken = typeToken.resolveType(getClass());
        typeToken = typeToken.resolveType(DataExtensionInitializer.class.getTypeParameters()[0]);
        //noinspection unchecked
        return (Class) typeToken.getRawType();
    }
}
