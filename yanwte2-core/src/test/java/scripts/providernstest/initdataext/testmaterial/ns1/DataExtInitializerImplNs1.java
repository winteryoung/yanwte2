package scripts.providernstest.initdataext.testmaterial.ns1;

import com.github.winteryoung.yanwte2.core.DataExtInitializer;
import scripts.providernstest.initdataext.testmaterial.Context;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public class DataExtInitializerImplNs1 implements DataExtInitializer<Context, DataExt> {
    private static int counter = 0;

    public static int getCounter() {
        return counter;
    }

    @Override
    public DataExt apply(Context context) {
        counter++;
        DataExt dataExt = new DataExt();
        dataExt.setI(3);
        return dataExt;
    }
}
