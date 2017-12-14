package scripts.providernstest.setdataext.testmaterial;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import scripts.providernstest.setdataext.testmaterial.ns1.Service1Provider;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service1Orchestrator implements ServiceOrchestrator {
    @Override
    public Combinator tree() {
        return provider(Service1Provider.class);
    }
}
