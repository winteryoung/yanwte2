package scripts.dataexttest.friends.testmaterial.legal.ns6_1;

import scripts.dataexttest.friends.testmaterial.legal.Context6;
import scripts.dataexttest.friends.testmaterial.legal.Service6;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class Service6Provider1 implements Service6 {
    @Override
    public Void apply(Context6 context6) {
        DataExt6_1 dataExt6_1 = context6.getDataExt();
        dataExt6_1.setI(3);
        return null;
    }
}
