package scripts.dataext.initdataext.testmaterial.multicontexts;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;

import java.util.function.Function;

/**
 * @author fanshen
 * @since 2017/12/19
 */
public interface Service16 extends Function<Context16, String>, ServiceOrchestrator<Service16> {
    @Override
    default Combinator tree() {
        return provider(Service16Provider.class);
    }
}
