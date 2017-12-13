package scripts.providernstest.testmaterial.onens.ns1;

import scripts.providernstest.testmaterial.onens.Context;
import scripts.providernstest.testmaterial.onens.Service1;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service1Provider implements Service1 {
    @Override
    public Void apply(Context context) {
        DataExt dataExt = new DataExt();
        dataExt.setI(3);

        context.setDataExtension(dataExt);

        return null;
    }
}
