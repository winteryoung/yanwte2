package scripts.performance.testmaterial;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;

import java.util.function.Function;

/**
 * @author fanshen
 * @since 2017/12/29
 */
public interface Service18 extends Function<String, String>, ServiceOrchestrator<Service18> {
    @Override
    default Combinator tree() {
        return provider(Service18Provider.class);
    }
}
