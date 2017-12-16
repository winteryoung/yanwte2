package scripts.dataext.friends.testmaterial.illegal.ns7_2;

import scripts.dataext.friends.testmaterial.illegal.Context7;
import scripts.dataext.friends.testmaterial.illegal.Service7;
import scripts.dataext.friends.testmaterial.legal.ns6_1.DataExt6_1;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class Service7Provider2 implements Service7 {
    @Override
    public Void apply(Context7 context7) {
        DataExt6_1 dataExt6_1 =
                context7.getDataExt("scripts.dataext.friends.testmaterial.illegal.ns7_1");
        dataExt6_1.setI(4);
        return null;
    }
}
