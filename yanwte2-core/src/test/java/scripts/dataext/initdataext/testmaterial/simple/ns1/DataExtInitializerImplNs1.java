package scripts.dataext.initdataext.testmaterial.simple.ns1;

import com.github.winteryoung.yanwte2.core.DataExtensionInitializer;
import scripts.dataext.initdataext.testmaterial.Context;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public class DataExtInitializerImplNs1 implements DataExtensionInitializer<Context, DataExt> {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static int getCounter() {
        return counter.get();
    }

    @Override
    public DataExt createDataExtension(Context context) {
        counter.incrementAndGet();
        DataExt dataExt = new DataExt();
        dataExt.setI(3);
        return dataExt;
    }
}
