package scripts.dataext.friends.testmaterial.legal.ns6_1;

import com.github.winteryoung.yanwte2.core.DataExtension;
import com.github.winteryoung.yanwte2.core.DataExtensionInitializer;
import org.assertj.core.util.Sets;
import scripts.dataext.friends.testmaterial.legal.Context6;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class DataExt6_1 implements DataExtension, DataExtensionInitializer<Context6, DataExt6_1> {
    private int i;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    @Override
    public Set<String> getFriendProviderPackages() {
        counter.incrementAndGet();
        return Sets.newLinkedHashSet("scripts.dataext.friends.testmaterial.legal.ns6_2");
    }

    @Override
    public DataExt6_1 createDataExtension(Context6 context6) {
        return new DataExt6_1();
    }

    private static AtomicInteger counter = new AtomicInteger(0);

    public static AtomicInteger getCounter() {
        return counter;
    }
}
