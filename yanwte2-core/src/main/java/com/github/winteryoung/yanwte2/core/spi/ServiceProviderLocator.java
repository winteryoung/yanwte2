package com.github.winteryoung.yanwte2.core.spi;

import java.net.URI;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/10
 */
public interface ServiceProviderLocator {
    Function<Object, Object> getProvider(URI providerURI);

    Set<Function<Object, Object>> getProviders(Class<? extends Function> serviceType);
}
