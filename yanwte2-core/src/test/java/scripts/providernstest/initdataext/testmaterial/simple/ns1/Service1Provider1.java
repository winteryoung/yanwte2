package scripts.providernstest.initdataext.testmaterial.simple.ns1;

import scripts.providernstest.initdataext.testmaterial.Context;
import scripts.providernstest.initdataext.testmaterial.simple.Service1;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service1Provider1 implements Service1 {
    @Override
    public Integer apply(Context context) {
        context.getDataExt();
        return null;
    }
}
