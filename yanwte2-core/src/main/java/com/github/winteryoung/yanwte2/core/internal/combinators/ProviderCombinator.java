package com.github.winteryoung.yanwte2.core.internal.combinators;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.winteryoung.yanwte2.core.internal.ServiceProviderLocators;
import com.github.winteryoung.yanwte2.core.internal.dataext.CurrentThreadProviderPackage;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.core.spi.LeafCombinator;
import com.google.common.collect.ImmutableList;
import java.net.URI;
import java.util.List;
import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/10
 */
public class ProviderCombinator implements LeafCombinator {
    private Function<Object, Object> provider;
    private String providerPackage;

    public ProviderCombinator(URI providerURI) {
        this(findProvider(providerURI));
    }

    private static Function<?, ?> findProvider(URI providerURI) {
        Function<?, ?> provider = ServiceProviderLocators.locateProvider(providerURI);
        return checkNotNull(provider, "Cannot find provider: " + providerURI);
    }

    ProviderCombinator(Function<?, ?> provider) {
        //noinspection unchecked
        this.provider = checkNotNull((Function) provider);
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

    @Override
    public String toString() {
        return provider.getClass().getName();
    }
}
