package scripts.dataext.initdataext.testmaterial.multicontexts;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;

import java.util.function.Function;

/**
 * @author fanshen
 * @since 2017/12/19
 */
public interface Service15 extends Function<Context15, String>, ServiceOrchestrator<Service15> {
    @Override
    default Combinator tree() {
        return provider(Service15Provider.class);
    }
}
