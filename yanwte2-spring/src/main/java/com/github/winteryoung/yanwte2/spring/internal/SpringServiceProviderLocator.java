package com.github.winteryoung.yanwte2.spring.internal;

import com.github.winteryoung.yanwte2.core.spi.ServiceProviderLocator;
import org.springframework.context.ApplicationContext;

import java.net.URI;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author fanshen
 * @since 2017/12/20
 */
public class SpringServiceProviderLocator implements ServiceProviderLocator {
    @Override
    public Function<?, ?> getProvider(URI providerURI) {
        if (!providerURI.getScheme().toLowerCase().equals("spring")) {
            return null;
        }

        String beanName = providerURI.getSchemeSpecificPart();

        ApplicationContext applicationContext = SpringHook.getApplicationContext();
        checkState(applicationContext != null);

        Object bean = applicationContext.getBean(beanName);

        if (bean instanceof Function) {
            return (Function<?, ?>) bean;
        }
        return null;
    }

    @Override
    public Set<Function> getProviders(Class<? extends Function> serviceType) {
        return null;
    }
}
