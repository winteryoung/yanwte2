package scripts.dynamicprovidercombinator.testmaterial.service8;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.google.common.base.Strings;

import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public interface Service8 extends Function<String, String>, ServiceOrchestrator<Service8> {
    @Override
    default Combinator tree() {
        return mapReduce((String a, String b) -> {
            return Strings.nullToEmpty(a) + Strings.nullToEmpty(b);
        }, dynamicProviders());
    }
}
