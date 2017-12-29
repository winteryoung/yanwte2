package springscripts.springorchestrator.testmaterial;

import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.spring.SpringServiceOrchestrator;

import java.util.function.Function;

/**
 * @author fanshen
 * @since 2017/12/22
 */
public interface Service1 extends Function<String, String>, SpringServiceOrchestrator<Service1> {
    @Override
    default Combinator tree() {
        return springProvider("service1Provider");
    }
}
