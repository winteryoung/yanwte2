package springscripts.dynamicprovidercombinator.testmaterial.n2;

import org.springframework.stereotype.Service;
import springscripts.dynamicprovidercombinator.testmaterial.Service2;

/**
 * @author fanshen
 * @since 2017/12/22
 */
@Service("service2Provider2")
public class Service2Provider2 implements Service2 {
    @Override
    public String apply(String s) {
        return "-2";
    }
}
