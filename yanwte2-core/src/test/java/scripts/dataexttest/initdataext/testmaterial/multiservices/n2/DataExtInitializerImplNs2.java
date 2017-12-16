package scripts.dataexttest.initdataext.testmaterial.multiservices.n2;

import com.github.winteryoung.yanwte2.core.DataExtensionInitializer;
import java.util.concurrent.atomic.AtomicInteger;
import scripts.dataexttest.initdataext.testmaterial.Context;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public class DataExtInitializerImplNs2 implements DataExtensionInitializer<Context, DataExtN2> {
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
