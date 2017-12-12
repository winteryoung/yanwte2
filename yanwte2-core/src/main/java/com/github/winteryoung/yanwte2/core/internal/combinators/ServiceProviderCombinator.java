package com.github.winteryoung.yanwte2.core.internal.combinators;

import com.github.winteryoung.yanwte2.core.spi.Combinator;
import java.net.URI;
import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/10
 */
public class ServiceProviderCombinator implements Combinator {
    private Function<Object, Object> provider;

    public ServiceProviderCombinator(URI providerURI, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }

        if (!providerURI.getScheme().toLowerCase().equals("class")) {
            throw new IllegalStateException("Unsupported provider URI scheme: " + providerURI);
        }

        String className = providerURI.getSchemeSpecificPart();
        Class<?> providerClass;
        try {
            providerClass = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (!Function.class.isAssignableFrom(providerClass)) {
            throw new RuntimeException(
                    "Service provider is expected to implement java.util.function.Function: "
                            + providerClass);
        }

        try {
            //noinspection unchecked
            this.provider = (Function<Object, Object>) providerClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object invoke(Object arg) {
        return provider.apply(arg);
    }
}
