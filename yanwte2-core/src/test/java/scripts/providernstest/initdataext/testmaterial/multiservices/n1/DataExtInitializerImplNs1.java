package scripts.providernstest.initdataext.testmaterial.multiservices.n1;

import com.github.winteryoung.yanwte2.core.DataExtInitializer;
import java.util.concurrent.atomic.AtomicInteger;
import scripts.providernstest.initdataext.testmaterial.Context;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public class DataExtInitializerImplNs1 implements DataExtInitializer<Context, DataExtN1> {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static int getCounter() {
        return counter.get();
    }

    @Override
    public DataExtN1 apply(Context context) {
        counter.incrementAndGet();
        DataExtN1 dataExt = new DataExtN1();
        dataExt.setI(3);
        return dataExt;
    }
}
