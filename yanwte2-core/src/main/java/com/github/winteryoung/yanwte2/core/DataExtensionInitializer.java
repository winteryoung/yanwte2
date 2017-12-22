package com.github.winteryoung.yanwte2.core;

import com.google.common.reflect.TypeToken;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public interface DataExtensionInitializer<ED extends ExtensibleData, DataExtension> {
    DataExtension createDataExtension(ED extensibleData);

    @SuppressWarnings("unchecked")
    default Class<ED> getExtensibleDataType() {
        TypeToken<?> typeToken =
                new TypeToken<DataExtensionInitializer<ED, DataExtension>>() {
                    private static final long serialVersionUID = 4639419131976092626L;
                };
        typeToken = typeToken.resolveType(getClass());
        typeToken = typeToken.resolveType(DataExtensionInitializer.class.getTypeParameters()[0]);
        return (Class) typeToken.getRawType();
    }
}
