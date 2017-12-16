package scripts.dataexttest.friends.testmaterial.illegal;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import java.util.function.Function;
import scripts.dataexttest.friends.testmaterial.illegal.ns7_1.Service7Provider1;
import scripts.dataexttest.friends.testmaterial.illegal.ns7_2.Service7Provider2;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public interface Service7 extends Function<Context7, Void>, ServiceOrchestrator<Service7> {
    @Override
    default Combinator tree() {
        return chain(provider(Service7Provider1.class), provider(Service7Provider2.class));
    }
}
