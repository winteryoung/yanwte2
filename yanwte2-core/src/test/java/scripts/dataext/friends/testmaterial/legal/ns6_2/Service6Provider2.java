package scripts.dataext.friends.testmaterial.legal.ns6_2;

import scripts.dataext.friends.testmaterial.legal.Context6;
import scripts.dataext.friends.testmaterial.legal.Service6;
import scripts.dataext.friends.testmaterial.legal.ns6_1.DataExt6_1;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class Service6Provider2 implements Service6 {
    @Override
    public Void apply(Context6 context6) {
        DataExt6_1 dataExt6_1 = context6.getDataExt("scripts.dataext.friends.testmaterial.legal.ns6_1");
        dataExt6_1.setI(4);
        return null;
    }
}
