package com.github.winteryoung.yanwte2.core;

import com.github.winteryoung.yanwte2.core.internal.ServiceOrchestratorLoader;
import com.github.winteryoung.yanwte2.core.internal.combinators.ServiceProviderCombinator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Winter Young
 * @since 2017/12/10
 */
public interface ServiceOrchestrator {
    Combinator tree();

    default Combinator provider(Class<?> providerClass) {
        URI providerURI;
        try {
            providerURI = new URI("class:" + providerClass.getName());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return new ServiceProviderCombinator(providerURI, providerClass.getClassLoader());
    }

    static <T> T getOrchestratorByServiceType(Class<T> serviceType) {
        return ServiceOrchestratorLoader.getOrchestratorByServiceType(serviceType);
    }
}
