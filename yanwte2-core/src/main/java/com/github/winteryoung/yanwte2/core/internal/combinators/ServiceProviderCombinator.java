package com.github.winteryoung.yanwte2.core.internal.combinators;

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

    public ServiceProviderCombinator(URI providerURI, ClassLoader classLoader) {
        ClassLoader backupClassLoader = null;
        if (classLoader != null) {
            backupClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(classLoader);
        }

        try {
            this.provider = ServiceProviderLocator.locateProvider(providerURI);
        } finally {
            if (backupClassLoader != null) {
                Thread.currentThread().setContextClassLoader(backupClassLoader);
            }
        }
    }

    @Override
    public Object invoke(Object arg) {
        return provider.apply(arg);
    }
}
