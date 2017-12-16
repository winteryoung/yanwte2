package scripts.dataext.initdataext.testmaterial.multiservices.n1;

import com.github.winteryoung.yanwte2.core.DataExtensionInitializer;
import java.util.concurrent.atomic.AtomicInteger;
import scripts.dataext.initdataext.testmaterial.Context;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public class DataExtInitializerImplNs1 implements DataExtensionInitializer<Context, DataExtN1> {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static int getCounter() {
        return counter.get();
    }

    @Override
    public DataExtN1 createDataExtension(Context context) {
        counter.incrementAndGet();
        DataExtN1 dataExt = new DataExtN1();
        dataExt.setI(3);
        return dataExt;
    }
}
