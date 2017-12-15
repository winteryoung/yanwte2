package scripts.providernstest.initdataext.testmaterial.multiservices.n2;

import scripts.providernstest.initdataext.testmaterial.Context;
import scripts.providernstest.initdataext.testmaterial.multiservices.MultiService2;

/**
 * @author fanshen
 * @since 2017/12/15
 */
public class MultiService2N2Provider implements MultiService2 {
    @Override
    public Void apply(Context context) {
        DataExtN2 dataExt = context.getDataExt();
        dataExt.setI(dataExt.getI() + 1);
        return null;
    }
}
