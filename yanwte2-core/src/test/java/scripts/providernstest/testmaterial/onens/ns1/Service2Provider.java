package scripts.providernstest.testmaterial.onens.ns1;

import scripts.providernstest.testmaterial.onens.Context;
import scripts.providernstest.testmaterial.onens.Service2;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service2Provider implements Service2 {
    @Override
    public Integer apply(Context context) {
        DataExt dataExt = context.getDataExtension();
        return dataExt.getI();
    }
}
