package scripts.providernstest.initdataext.testmaterial.simple.ns2;

import com.github.winteryoung.yanwte2.core.DataExtInitializer;
import scripts.providernstest.initdataext.testmaterial.Context;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fanshen
 * @since 2017/12/14
 */
public class DataExtInitializerImplNs2 implements DataExtInitializer<Context, DataExt> {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static int getCounter() {
        return counter.get();
    }

    @Override
    public DataExt apply(Context context) {
        counter.incrementAndGet();
        return null;
    }
}
