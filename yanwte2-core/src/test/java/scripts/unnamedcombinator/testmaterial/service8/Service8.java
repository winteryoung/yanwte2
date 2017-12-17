package scripts.unnamedcombinator.testmaterial.service8;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;

import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public interface Service8 extends Function<String, String>, ServiceOrchestrator<Service8> {
    @Override
    default Combinator tree() {
        return mapReduce((String a, String b) -> a + b, unnamed());
    }
}
