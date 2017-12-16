package scripts.dataexttest.friends.testmaterial.legal;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import java.util.function.Function;

import scripts.dataexttest.friends.testmaterial.legal.ns6_1.Service6Provider1;
import scripts.dataexttest.friends.testmaterial.legal.ns6_2.Service6Provider2;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public interface Service6 extends Function<Context6, Void>, ServiceOrchestrator<Service6> {
    @Override
    default Combinator tree() {
        return chain(provider(Service6Provider1.class), provider(Service6Provider2.class));
    }
}
