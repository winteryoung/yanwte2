package com.github.winteryoung.yanwte2.core.internal.combinators;

import com.github.winteryoung.yanwte2.core.internal.dataext.CurrentThreadProviderNamespace;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.core.spi.ServiceProviderLocator;
import java.net.URI;
import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/10
 */
public class ServiceProviderCombinator implements Combinator {
    private Function<Object, Object> provider;
    private String providerNamespace;

    public ServiceProviderCombinator(URI providerURI) {
        this.provider = ServiceProviderLocator.locateProvider(providerURI);
        this.providerNamespace = provider.getClass().getPackage().getName();
    }

    @Override
    public Object invoke(Object arg) {
        try {
            CurrentThreadProviderNamespace.set(providerNamespace);
            return provider.apply(arg);
        } finally {
            CurrentThreadProviderNamespace.set(null);
        }
    }
}
