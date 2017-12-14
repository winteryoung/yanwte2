package scripts.providernstest.initdataext.testmaterial;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import scripts.providernstest.initdataext.testmaterial.ns2.Service2Provider1;
import scripts.providernstest.initdataext.testmaterial.ns2.Service2Provider2;
import scripts.providernstest.initdataext.testmaterial.ns3.Service3Provider1;
import scripts.providernstest.initdataext.testmaterial.ns3.Service3Provider2;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service3Orchestrator implements ServiceOrchestrator {
    @Override
    public Combinator tree() {
        return chain(
                provider(Service3Provider1.class),
                provider(Service3Provider2.class)
        );
    }
}
