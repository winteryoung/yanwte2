package scripts.serviceorchestratortest.testmaterial.orchestratorwithoutgenerictype;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;

/**
 * @author fanshen
 * @since 2017/12/12
 */
public class Service5Orchestrator implements ServiceOrchestrator {
    @Override
    public Combinator tree() {
        return provider(Service5Provider.class);
    }
}
