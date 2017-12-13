package com.github.winteryoung.yanwte2.core;

import com.github.winteryoung.yanwte2.core.internal.providerns.CurrentThreadProviderNamespace;
import com.github.winteryoung.yanwte2.core.internal.providerns.DataExtensionCache;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public interface ExtensibleData {
    default <T> T getDataExtension() {
        //noinspection unchecked
        return (T) DataExtensionCache.get(this, CurrentThreadProviderNamespace.get());
    }

    default void setDataExtension(Object extension) {
        DataExtensionCache.put(this, CurrentThreadProviderNamespace.get(), extension);
    }
}
