package scripts.serviceorchestratortest.testmaterial.simple;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;

/**
 * @author fanshen
 * @since 2017/12/11
 */
public class NormalServiceOrchestrator implements ServiceOrchestrator {
    @Override
    public Combinator tree() {
        return arg -> (Integer) arg + 3;
    }
}
