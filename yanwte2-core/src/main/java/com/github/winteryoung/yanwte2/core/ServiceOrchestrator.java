package com.github.winteryoung.yanwte2.core;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.winteryoung.yanwte2.core.internal.ServiceOrchestratorLoader;
import com.github.winteryoung.yanwte2.core.internal.combinators.ChainCombinator;
import com.github.winteryoung.yanwte2.core.internal.combinators.ServiceProviderCombinator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.google.common.collect.Lists;
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

        return new ServiceProviderCombinator(providerURI);
    }

    default Combinator chain(Combinator... combinators) {
        checkNotNull(combinators);
        return new ChainCombinator(Lists.newArrayList(combinators));
    }

    static <T> T getOrchestrator(Class<T> serviceType) {
        return ServiceOrchestratorLoader.getOrchestratorByServiceType(serviceType);
    }
}
