package com.github.winteryoung.yanwte2.core.spi;

import com.github.winteryoung.yanwte2.core.DataExtensionInitializer;
import com.github.winteryoung.yanwte2.core.ExtensibleData;

import java.util.Set;

/**
 * @author fanshen
 * @since 2017/12/22
 */
public interface DataExtensionInitializerLocator {
    Set<DataExtensionInitializer<ExtensibleData, ?>> getInitializers();
}
