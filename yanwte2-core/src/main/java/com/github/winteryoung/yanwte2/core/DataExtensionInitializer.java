package com.github.winteryoung.yanwte2.core;

import java.util.function.Function;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public interface DataExtensionInitializer<ExtensibleData, DataExtension> {
    DataExtension createDataExtension(ExtensibleData extensibleData);
}
