package com.github.winteryoung.yanwte2.spring;

import static com.google.common.base.Preconditions.checkState;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.internal.combinators.ProviderCombinator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;

/**
 * @author fanshen
 * @since 2017/12/20
 */
public interface SpringServiceOrchestrator<T extends Function> extends ServiceOrchestrator<T> {
    default Combinator springProvider(String beanId) {
        Class<? extends Function> serviceType = getServiceType();
        checkState(
                serviceType != Function.class,
                "Generic type parameter is required for orchestrator: " + getClass().getName());

        URI providerURI;
        try {
            providerURI = new URI("spring:" + beanId);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return new ProviderCombinator(providerURI);
    }
}
