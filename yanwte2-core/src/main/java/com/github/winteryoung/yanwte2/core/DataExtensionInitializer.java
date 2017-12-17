package com.github.winteryoung.yanwte2.core;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public interface DataExtensionInitializer<ExtensibleData, DataExtension> {
    DataExtension createDataExtension(ExtensibleData extensibleData);
}
