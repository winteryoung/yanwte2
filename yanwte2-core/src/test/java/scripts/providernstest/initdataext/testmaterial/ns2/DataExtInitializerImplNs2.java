package scripts.providernstest.initdataext.testmaterial.ns2;

import com.github.winteryoung.yanwte2.core.DataExtInitializer;
import scripts.providernstest.initdataext.testmaterial.Context;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public class DataExtInitializerImplNs2 implements DataExtInitializer<Context, DataExt> {
    private static int counter = 0;

    public static int getCounter() {
        return counter;
    }

    @Override
    public DataExt apply(Context context) {
        counter++;
        return null;
    }
}
