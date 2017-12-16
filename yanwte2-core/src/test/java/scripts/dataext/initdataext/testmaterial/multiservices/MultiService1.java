package scripts.dataext.initdataext.testmaterial.multiservices;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import scripts.dataext.initdataext.testmaterial.Context;
import scripts.dataext.initdataext.testmaterial.multiservices.n1.MultiService1N1Provider;
import scripts.dataext.initdataext.testmaterial.multiservices.n2.MultiService1N2Provider;

import java.util.function.Function;

/**
 * @author fanshen
 * @since 2017/12/15
 */
public interface MultiService1 extends Function<Context, Void>, ServiceOrchestrator<MultiService1> {
    @Override
    default Combinator tree() {
        return chain(
                provider(MultiService1N1Provider.class), provider(MultiService1N2Provider.class));
    }
}
