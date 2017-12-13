package scripts.serviceorchestratortest.testmaterial;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;

/**
 * @author fanshen
 * @since 2017/12/12
 */
public class SimpleServiceOrchestrator implements ServiceOrchestrator {
    @Override
    public Combinator tree() {
        return provider(SimpleServiceProvider.class);
    }
}
