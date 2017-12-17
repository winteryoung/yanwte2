package com.github.winteryoung.yanwte2.core.internal.combinators;

import com.github.winteryoung.yanwte2.core.internal.ServiceProviderLocators;
import com.github.winteryoung.yanwte2.core.internal.dataext.CurrentThreadProviderPackage;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.core.spi.LeafCombinator;
import com.github.winteryoung.yanwte2.core.spi.ServiceProviderLocator;
import com.google.common.collect.ImmutableList;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Winter Young
 * @since 2017/12/10
 */
public class ServiceProviderCombinator implements LeafCombinator {
    private Function<Object, Object> provider;
    private String providerPackage;

    public ServiceProviderCombinator(URI providerURI) {
        this(ServiceProviderLocators.locateProvider(providerURI));
    }

    ServiceProviderCombinator(Function<Object, Object> provider) {
        this.provider = checkNotNull(provider);
        this.providerPackage = provider.getClass().getPackage().getName();
    }

    public String getProviderPackage() {
        return providerPackage;
    }

    @Override
    public Object invoke(Object arg) {
        try {
            CurrentThreadProviderPackage.set(providerPackage);
            return provider.apply(arg);
        } finally {
            CurrentThreadProviderPackage.set(null);
        }
    }

    @Override
    public List<Combinator> getChildren() {
        return ImmutableList.of();
    }
}
