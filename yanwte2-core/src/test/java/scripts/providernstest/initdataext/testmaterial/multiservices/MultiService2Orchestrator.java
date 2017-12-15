package scripts.providernstest.initdataext.testmaterial.multiservices;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import scripts.providernstest.initdataext.testmaterial.multiservices.n1.MultiService2N1Provider;
import scripts.providernstest.initdataext.testmaterial.multiservices.n2.MultiService2N2Provider;

/**
 * @author fanshen
 * @since 2017/12/15
 */
public class MultiService2Orchestrator implements ServiceOrchestrator<MultiService2> {
    @Override
    public Combinator tree() {
        return chain(
                provider(MultiService2N1Provider.class), provider(MultiService2N2Provider.class));
    }
}
