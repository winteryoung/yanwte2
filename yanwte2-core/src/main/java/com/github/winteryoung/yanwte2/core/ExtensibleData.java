package com.github.winteryoung.yanwte2.core;

import com.github.winteryoung.yanwte2.core.internal.dataext.CurrentThreadProviderPackage;
import com.github.winteryoung.yanwte2.core.internal.dataext.DataExtensions;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public interface ExtensibleData {
    default <T> T getDataExt() {
        return getDataExt(CurrentThreadProviderPackage.get());
    }

    default void setDataExt(Object extension) {
        DataExtensions.put(this, CurrentThreadProviderPackage.get(), extension);
    }

    @SuppressWarnings("unchecked")
    default <T> T getDataExt(String providerPackage) {
        return (T) DataExtensions.get(this, providerPackage, CurrentThreadProviderPackage.get());
    }
}
