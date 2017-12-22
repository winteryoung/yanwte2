package scripts.springorchestrator.testmaterial;

import org.springframework.stereotype.Service;

/**
 * @author fanshen
 * @since 2017/12/22
 */
@Service("service1Provider")
public class Service1Provider implements Service1 {
    @Override
    public String apply(String s) {
        return s + "-1";
    }
}
