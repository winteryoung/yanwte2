package springscripts.unnamedcombinator.testmaterial.n1;

import org.springframework.stereotype.Service;
import springscripts.unnamedcombinator.testmaterial.Service2;

/**
 * @author fanshen
 * @since 2017/12/22
 */
@Service("service2Provider1")
public class Service2Provider1 implements Service2 {
    @Override
    public String apply(String s) {
        return s + "-1";
    }
}
