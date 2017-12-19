package scripts.dataext.initdataext.testmaterial.simple;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import scripts.dataext.initdataext.testmaterial.Context;
import scripts.dataext.initdataext.testmaterial.simple.ns2.Service2Provider1;

import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public interface Service2 extends Function<Context, Integer>, ServiceOrchestrator<Service2> {
    @Override
    default Combinator tree() {
        return provider(Service2Provider1.class);
    }
}
