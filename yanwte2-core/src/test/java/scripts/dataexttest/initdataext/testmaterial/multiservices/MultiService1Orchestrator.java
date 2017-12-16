package scripts.dataexttest.initdataext.testmaterial.multiservices;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import scripts.dataexttest.initdataext.testmaterial.multiservices.n1.MultiService1N1Provider;
import scripts.dataexttest.initdataext.testmaterial.multiservices.n2.MultiService1N2Provider;

/**
 * @author fanshen
 * @since 2017/12/15
 */
public class MultiService1Orchestrator implements ServiceOrchestrator<MultiService1> {
    @Override
    public Combinator tree() {
        return chain(
                provider(MultiService1N1Provider.class), provider(MultiService1N2Provider.class));
    }
}
