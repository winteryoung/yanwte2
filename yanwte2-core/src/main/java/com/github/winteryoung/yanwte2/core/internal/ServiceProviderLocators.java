package com.github.winteryoung.yanwte2.core.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.winteryoung.yanwte2.core.spi.ServiceProviderLocator;
import com.google.common.collect.*;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class ServiceProviderLocators {
    private static List<ServiceProviderLocator> serviceProviderLocators = Lists.newArrayList();

    static {
        for (ServiceProviderLocator serviceProviderLocator :
                ServiceLoader.load(ServiceProviderLocator.class)) {
            serviceProviderLocators.add(serviceProviderLocator);
        }
    }

    public static Function<?, ?> locateProvider(URI providerURI) {
        checkNotNull(providerURI);

        for (ServiceProviderLocator serviceProviderLocator : serviceProviderLocators) {
            Function<?, ?> provider = serviceProviderLocator.getProvider(providerURI);
            if (provider != null) {
                return provider;
            }
        }

        return null;
    }

    private static Set<Function<?, ?>> locateProviders(Class<? extends Function<?, ?>> serviceType) {
        Set<Function<?, ?>> providers = Sets.newHashSet();

        for (ServiceProviderLocator serviceProviderLocator : serviceProviderLocators) {
            Set<Function<?, ?>> _providers = serviceProviderLocator.getProviders(serviceType);
            if (_providers != null) {
                providers.addAll(_providers);
            }
        }

        return providers;
    }

    public static Map<String, Function<?, ?>> locateAllProvidersIndexedByPackages(
            Class<? extends Function<?, ?>> serviceType) {
        Set<Function<?, ?>> providers = locateProviders(serviceType);
        return Maps.uniqueIndex(
                providers, provider -> checkNotNull(provider).getClass().getPackage().getName());
    }
}
