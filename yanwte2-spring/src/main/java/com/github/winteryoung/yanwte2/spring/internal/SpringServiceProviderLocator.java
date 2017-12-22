package com.github.winteryoung.yanwte2.spring.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.github.winteryoung.yanwte2.core.spi.ServiceProviderLocator;
import com.google.common.collect.ImmutableSet;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.springframework.context.ApplicationContext;

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
    public Set<Function<?, ?>> getProviders(Class<? extends Function<?, ?>> serviceType) {
        checkNotNull(serviceType);

        ApplicationContext applicationContext = SpringHook.getApplicationContext();
        checkState(applicationContext != null);

        @SuppressWarnings("unchecked")
        Map<String, Function<?, ?>> beansOfType =
                (Map) applicationContext.getBeansOfType(serviceType);

        if (beansOfType != null && !beansOfType.isEmpty()) {
            Collection<? extends Function<?, ?>> beans = beansOfType.values();
            return ImmutableSet.copyOf(beans);
        }

        return ImmutableSet.of();
    }
}
