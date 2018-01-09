package scripts.dynamicprovidercombinator.testmaterial.service9;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import java.util.function.Function;
import scripts.dynamicprovidercombinator.testmaterial.service9.n1.Service9Provider1;

/**
 * @author Winter Young
 * @since 2017/12/17
 */
public interface Service9 extends Function<String, String>, ServiceOrchestrator<Service9> {
    @Override
    default Combinator tree() {
        return mapReduce(
                (String a, String b) -> a + b, provider(Service9Provider1.class), dynamicProviders());
    }
}
