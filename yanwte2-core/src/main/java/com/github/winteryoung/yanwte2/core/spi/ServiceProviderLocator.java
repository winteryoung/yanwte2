package com.github.winteryoung.yanwte2.core.spi;

import com.github.winteryoung.yanwte2.core.internal.ServiceProviderLocatorStaticImpl;

import java.net.URI;
import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/10
 */
public interface ServiceProviderLocator {
    Function<Object, Object> getProvider(URI providerURI);

    static Function<Object, Object> locateProvider(URI providerURI) {
        return ServiceProviderLocatorStaticImpl.locateProvider(providerURI);
    }
}
