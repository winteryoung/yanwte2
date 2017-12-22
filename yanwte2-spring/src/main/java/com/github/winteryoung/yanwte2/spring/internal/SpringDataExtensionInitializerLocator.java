package com.github.winteryoung.yanwte2.spring.internal;

import com.github.winteryoung.yanwte2.core.DataExtensionInitializer;
import com.github.winteryoung.yanwte2.core.ExtensibleData;
import com.github.winteryoung.yanwte2.core.spi.DataExtensionInitializerLocator;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.springframework.context.ApplicationContext;

/**
 * @author fanshen
 * @since 2017/12/22
 */
public class SpringDataExtensionInitializerLocator implements DataExtensionInitializerLocator {
    @Override
    public Set<DataExtensionInitializer<ExtensibleData, ?>> getInitializers() {
        ApplicationContext applicationContext = SpringHook.getApplicationContext();
        @SuppressWarnings("unchecked")
        Map<String, DataExtensionInitializer<ExtensibleData, ?>> beansOfType =
                (Map) applicationContext.getBeansOfType(DataExtensionInitializer.class);
        if (beansOfType != null) {
            Collection<DataExtensionInitializer<ExtensibleData, ?>> initializers =
                    beansOfType.values();
            return ImmutableSet.copyOf(initializers);
        }
        return ImmutableSet.of();
    }
}
