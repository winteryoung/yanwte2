package scripts.emptycombinator.testmaterial;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;

import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/17
 */
public interface Service12 extends Function<String, String>, ServiceOrchestrator<Service12> {
    @Override
    default Combinator tree() {
        return empty();
    }
}
