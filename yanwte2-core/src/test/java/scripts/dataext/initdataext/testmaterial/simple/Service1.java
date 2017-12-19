package scripts.dataext.initdataext.testmaterial.simple;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import scripts.dataext.initdataext.testmaterial.Context;
import scripts.dataext.initdataext.testmaterial.simple.ns1.Service1Provider1;

import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public interface Service1 extends Function<Context, Integer>, ServiceOrchestrator<Service1> {
    default Combinator tree() {
        return provider(Service1Provider1.class);
    }
}
