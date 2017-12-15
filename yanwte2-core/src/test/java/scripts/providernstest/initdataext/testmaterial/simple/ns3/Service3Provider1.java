package scripts.providernstest.initdataext.testmaterial.simple.ns3;

import scripts.providernstest.initdataext.testmaterial.Context;
import scripts.providernstest.initdataext.testmaterial.simple.Service2;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service3Provider1 implements Service2 {
    @Override
    public Integer apply(Context context) {
        context.getDataExt();
        return null;
    }
}
