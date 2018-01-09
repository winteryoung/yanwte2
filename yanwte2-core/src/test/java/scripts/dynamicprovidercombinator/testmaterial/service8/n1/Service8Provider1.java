package scripts.dynamicprovidercombinator.testmaterial.service8.n1;

import scripts.dynamicprovidercombinator.testmaterial.service8.Service8;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class Service8Provider1 implements Service8 {
    @Override
    public String apply(String s) {
        return s + "-1";
    }
}
