package scripts.dataexttest.initdataext.testmaterial.simple.ns2;

import scripts.dataexttest.initdataext.testmaterial.Context;
import scripts.dataexttest.initdataext.testmaterial.simple.Service2;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service2Provider2 implements Service2 {
    @Override
    public Integer apply(Context context) {
        context.getDataExt();
        return null;
    }
}
