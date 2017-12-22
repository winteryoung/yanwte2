package com.github.winteryoung.yanwte2.core;

import com.github.winteryoung.yanwte2.core.internal.ServiceOrchestratorLoader;
import com.github.winteryoung.yanwte2.core.internal.combinators.*;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/10
 */
public interface ServiceOrchestrator<T extends Function<?, ?>> {
    Combinator tree();

    @SuppressWarnings("unchecked")
    default Class<? extends Function<?, ?>> getServiceType() {
        TypeToken<?> typeToken = new TypeToken<ServiceOrchestrator<T>>() {
            private static final long serialVersionUID = -4811029877591669034L;
        };
        typeToken = typeToken.resolveType(getClass());
        typeToken = typeToken.resolveType(ServiceOrchestrator.class.getTypeParameters()[0]);
        return (Class) typeToken.getRawType();
    }

    default Combinator provider(Class<? extends T> providerClass) {
        Class<? extends Function<?, ?>> serviceType = getServiceType();
        if (Function.class.equals(serviceType)) {
            throw new RuntimeException(
                    "Generic type parameter is required for orchestrator: " + getClass().getName());
        }

        URI providerURI;
        try {
            providerURI = new URI("class:" + providerClass.getName());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return new ProviderCombinator(providerURI);
    }

    default Combinator chain(Combinator... combinators) {
        return new ChainCombinator(Lists.newArrayList(combinators));
    }

    @SuppressWarnings("unchecked")
    default <R> Combinator mapReduce(BinaryOperator<R> reducer, Combinator... combinators) {
        return new MapReduceCombinator(Lists.newArrayList(combinators), (BinaryOperator) reducer);
    }

    @SuppressWarnings("unchecked")
    default Combinator unnamed() {
        Class<? super T> parameterType = new TypeToken<T>(getClass()) {
            private static final long serialVersionUID = 2794153935323127741L;
        }.getRawType();
        return new UnnamedCombinator(this, (Class) parameterType);
    }

    default Combinator empty() {
        return new EmptyCombinator();
    }

    static <T extends Function<?, ?>> T getOrchestrator(Class<T> serviceType) {
        return ServiceOrchestratorLoader.getOrchestratorByServiceType(serviceType);
    }
}
