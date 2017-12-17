package scripts.providercombinator.testmaterial;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;

import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/17
 */
public interface Service11 extends Function<String, String>, ServiceOrchestrator<Service11> {
    @Override
    default Combinator tree() {
        return chain(provider(Service11Provider1.class), provider(Service11Provider2.class));
    }
}
