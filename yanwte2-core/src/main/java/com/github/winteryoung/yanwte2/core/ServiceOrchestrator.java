package com.github.winteryoung.yanwte2.core;

import com.github.winteryoung.yanwte2.core.internal.ServiceOrchestratorLoader;
import com.github.winteryoung.yanwte2.core.spi.Combinator;

/**
 * @author Winter Young
 * @since 2017/12/10
 */
public interface ServiceOrchestrator {
    Combinator tree();

    static <T> T getOrchestratorByServiceType(Class<T> serviceType) {
        return ServiceOrchestratorLoader.getOrchestratorByServiceType(serviceType);
    }
}
