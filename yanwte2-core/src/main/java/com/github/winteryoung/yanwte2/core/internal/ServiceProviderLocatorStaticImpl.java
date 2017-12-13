package com.github.winteryoung.yanwte2.core.internal;

import com.github.winteryoung.yanwte2.core.spi.ServiceProviderLocator;
import com.google.common.collect.Lists;

import java.net.URI;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class ServiceProviderLocatorStaticImpl {
    private static List<ServiceProviderLocator> serviceProviderLocators = Lists.newArrayList();

    static {
        for (ServiceProviderLocator serviceProviderLocator :
                ServiceLoader.load(ServiceProviderLocator.class)) {
            serviceProviderLocators.add(serviceProviderLocator);
        }
    }

    public static Function<Object, Object> locateProvider(URI providerURI) {
        for (ServiceProviderLocator serviceProviderLocator : serviceProviderLocators) {
            Function<Object, Object> provider = serviceProviderLocator.getProvider(providerURI);
            if (provider != null) {
                return provider;
            }
        }
        return null;
    }
}
