package springscripts.springdataextinit.testmaterial;

import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.spring.SpringServiceOrchestrator;

import java.util.function.Function;

/**
 * @author fanshen
 * @since 2017/12/22
 */
public interface Service3 extends Function<Context3, Void>, SpringServiceOrchestrator<Service3> {
    @Override
    default Combinator tree() {
        return springProvider("service3Provider");
    }
}
