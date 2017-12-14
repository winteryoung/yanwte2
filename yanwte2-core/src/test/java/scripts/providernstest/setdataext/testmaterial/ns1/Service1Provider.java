package scripts.providernstest.setdataext.testmaterial.ns1;

import scripts.providernstest.setdataext.testmaterial.Context;
import scripts.providernstest.setdataext.testmaterial.Service1;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class Service1Provider implements Service1 {
    @Override
    public Void apply(Context context) {
        DataExt dataExt = new DataExt();
        dataExt.setI(3);

        context.setDataExt(dataExt);

        return null;
    }
}
