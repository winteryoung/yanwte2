package scripts.dataext.initdataext.testmaterial.simple;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import scripts.dataext.initdataext.testmaterial.simple.ns2.Service2Provider1;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service2Orchestrator implements ServiceOrchestrator<Service2> {
    @Override
    public Combinator tree() {
        return provider(Service2Provider1.class);
    }
}
