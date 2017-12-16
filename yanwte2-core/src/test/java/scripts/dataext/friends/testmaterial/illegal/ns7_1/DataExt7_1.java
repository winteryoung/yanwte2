package scripts.dataext.friends.testmaterial.illegal.ns7_1;

import com.github.winteryoung.yanwte2.core.DataExtension;
import com.github.winteryoung.yanwte2.core.DataExtensionInitializer;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.util.Sets;
import scripts.dataext.friends.testmaterial.illegal.Context7;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class DataExt7_1 implements DataExtension, DataExtensionInitializer<Context7, DataExt7_1> {
    private int i;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    @Override
    public Set<String> getFriendProviderPackages() {
        return Sets.newLinkedHashSet("scripts.dataext.friends.testmaterial.legal.ns7_1");
    }

    @Override
    public DataExt7_1 createDataExtension(Context7 context6) {
        return new DataExt7_1();
    }
}
