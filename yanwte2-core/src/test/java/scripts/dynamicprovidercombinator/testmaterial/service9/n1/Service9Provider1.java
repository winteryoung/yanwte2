package scripts.dynamicprovidercombinator.testmaterial.service9.n1;

import scripts.dynamicprovidercombinator.testmaterial.service9.Service9;

/**
 * @author Winter Young
 * @since 2017/12/17
 */
public class Service9Provider1 implements Service9 {
    @Override
    public String apply(String s) {
        return s + "-1";
    }
}
