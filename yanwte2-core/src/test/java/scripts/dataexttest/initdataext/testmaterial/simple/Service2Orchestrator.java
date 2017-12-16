package scripts.dataexttest.initdataext.testmaterial.simple;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import scripts.dataexttest.initdataext.testmaterial.simple.ns2.Service2Provider1;
import scripts.dataexttest.initdataext.testmaterial.simple.ns2.Service2Provider2;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service2Orchestrator implements ServiceOrchestrator<Service2> {
    @Override
    public Combinator tree() {
        return chain(provider(Service2Provider1.class), provider(Service2Provider2.class));
    }
}
