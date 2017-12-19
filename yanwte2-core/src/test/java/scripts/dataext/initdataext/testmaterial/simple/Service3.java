package scripts.dataext.initdataext.testmaterial.simple;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import scripts.dataext.initdataext.testmaterial.Context;
import scripts.dataext.initdataext.testmaterial.simple.ns3.Service3Provider1;

import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public interface Service3 extends Function<Context, Integer>, ServiceOrchestrator<Service3> {
    @Override
    default Combinator tree() {
        return chain(provider(Service3Provider1.class));
    }
}
