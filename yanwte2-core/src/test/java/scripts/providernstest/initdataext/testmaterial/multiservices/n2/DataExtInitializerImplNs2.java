package scripts.providernstest.initdataext.testmaterial.multiservices.n2;

import com.github.winteryoung.yanwte2.core.DataExtInitializer;
import java.util.concurrent.atomic.AtomicInteger;
import scripts.providernstest.initdataext.testmaterial.Context;
import scripts.providernstest.initdataext.testmaterial.simple.ns1.DataExt;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public class DataExtInitializerImplNs2 implements DataExtInitializer<Context, DataExtN2> {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static int getCounter() {
        return counter.get();
    }

    @Override
    public DataExtN2 apply(Context context) {
        counter.incrementAndGet();
        DataExtN2 dataExt = new DataExtN2();
        dataExt.setI(9);
        return dataExt;
    }
}
