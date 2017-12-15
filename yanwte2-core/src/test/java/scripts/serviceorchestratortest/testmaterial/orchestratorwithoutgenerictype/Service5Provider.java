package scripts.serviceorchestratortest.testmaterial.orchestratorwithoutgenerictype;

/**
 * @author fanshen
 * @since 2017/12/12
 */
public class Service5Provider implements Service5 {
    @Override
    public String apply(String s) {
        return s + "-abc";
    }
}
