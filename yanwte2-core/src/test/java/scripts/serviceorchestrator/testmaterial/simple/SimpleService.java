package scripts.serviceorchestrator.testmaterial.simple;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;

import java.util.function.Function;

/**
 * @author fanshen
 * @since 2017/12/12
 */
public interface SimpleService extends Function<String, String>, ServiceOrchestrator<SimpleService> {
    @Override
    default Combinator tree() {
        return provider(SimpleServiceProvider.class);
    }
}
