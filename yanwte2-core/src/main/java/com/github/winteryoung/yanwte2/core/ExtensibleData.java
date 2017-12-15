package com.github.winteryoung.yanwte2.core;

import com.github.winteryoung.yanwte2.core.internal.providerns.CurrentThreadProviderNamespace;
import com.github.winteryoung.yanwte2.core.internal.providerns.DataExtensions;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public interface ExtensibleData {
    default <T> T getDataExt() {
        return getDataExt(CurrentThreadProviderNamespace.get());
    }

    default void setDataExt(Object extension) {
        DataExtensions.put(this, CurrentThreadProviderNamespace.get(), extension);
    }

    default <T> T getDataExt(String providerNamespace) {
        //noinspection unchecked
        return (T) DataExtensions.get(this, providerNamespace);
    }
}
