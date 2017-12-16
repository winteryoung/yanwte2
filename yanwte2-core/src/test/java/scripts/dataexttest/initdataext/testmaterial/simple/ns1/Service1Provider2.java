package scripts.dataexttest.initdataext.testmaterial.simple.ns1;

import scripts.dataexttest.initdataext.testmaterial.Context;
import scripts.dataexttest.initdataext.testmaterial.simple.Service1;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service1Provider2 implements Service1 {
    @Override
    public Integer apply(Context context) {
        DataExt dataExt = context.getDataExt();
        return dataExt.getI();
    }
}
