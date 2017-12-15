package scripts.providernstest.initdataext.testmaterial.simple;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import scripts.providernstest.initdataext.testmaterial.simple.ns2.Service2Provider1;
import scripts.providernstest.initdataext.testmaterial.simple.ns2.Service2Provider2;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service2Orchestrator implements ServiceOrchestrator {
    @Override
    public Combinator tree() {
        return chain(
                provider(Service2Provider1.class),
                provider(Service2Provider2.class)
        );
    }
}
