package scripts.providernstest.initdataext.testmaterial.simple;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import scripts.providernstest.initdataext.testmaterial.simple.ns1.Service1Provider1;
import scripts.providernstest.initdataext.testmaterial.simple.ns1.Service1Provider2;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service1Orchestrator implements ServiceOrchestrator {
    @Override
    public Combinator tree() {
        return chain(provider(Service1Provider1.class), provider(Service1Provider2.class));
    }
}
