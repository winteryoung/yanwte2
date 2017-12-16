package scripts.dataexttest.friends.testmaterial.illegal.ns7_1;

import scripts.dataexttest.friends.testmaterial.illegal.Context7;
import scripts.dataexttest.friends.testmaterial.illegal.Service7;
import scripts.dataexttest.friends.testmaterial.legal.Context6;
import scripts.dataexttest.friends.testmaterial.legal.Service6;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class Service7Provider1 implements Service7 {
    @Override
    public Void apply(Context7 context7) {
        DataExt7_1 dataExt7_1 = context7.getDataExt();
        dataExt7_1.setI(3);
        return null;
    }
}
