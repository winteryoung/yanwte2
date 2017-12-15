package scripts.providernstest.initdataext.testmaterial.simple.ns3;

import scripts.providernstest.initdataext.testmaterial.Context;
import scripts.providernstest.initdataext.testmaterial.simple.Service2;
import scripts.providernstest.initdataext.testmaterial.simple.Service3;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service3Provider1 implements Service3 {
    @Override
    public Integer apply(Context context) {
        context.getDataExt();
        return null;
    }
}
