package scripts.serviceorchestrator.testmaterial.orchestratorwithoutgenerictype;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;

import java.util.function.Function;

/**
 * @author fanshen
 * @since 2017/12/12
 */
public interface Service5 extends Function<String, String>, ServiceOrchestrator {
    @Override
    default Combinator tree() {
        return provider(Service5Provider.class);
    }
}
