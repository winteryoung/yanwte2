package springscripts.dynamicprovidercombinator.testmaterial;

import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.spring.SpringServiceOrchestrator;

import java.util.function.Function;

/**
 * @author fanshen
 * @since 2017/12/22
 */
public interface Service2 extends Function<String, String>, SpringServiceOrchestrator<Service2> {
    @Override
    default Combinator tree() {
        return mapReduce((String s1, String s2) -> s1 + s2,
                springProvider("service2Provider1"),
                dynamicProviders());
    }
}
