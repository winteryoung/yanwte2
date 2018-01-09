package scripts.expandedtree.testmaterial;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import java.util.function.Function;

/**
 * @author fanshen
 * @since 2017/12/28
 */
public interface Service17 extends Function<String, String>, ServiceOrchestrator<Service17> {
    @Override
    default Combinator tree() {
        return mapReduce(
                (r1, r2) -> null,
                chain(
                        provider(Service17Provider1.class),
                        provider(Service17Provider2.class),
                        provider(Service17Provider3.class)),
                provider(Service17Provider3.class),
                dynamicProviders());
    }
}
