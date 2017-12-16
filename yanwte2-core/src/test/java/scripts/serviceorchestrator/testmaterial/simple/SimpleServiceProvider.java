package scripts.serviceorchestrator.testmaterial.simple;

/**
 * @author fanshen
 * @since 2017/12/12
 */
public class SimpleServiceProvider implements SimpleService {
    @Override
    public String apply(String s) {
        return s + "-abc";
    }
}
