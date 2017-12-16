package scripts.dataexttest.friends;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import scripts.dataexttest.friends.testmaterial.illegal.Context7;
import scripts.dataexttest.friends.testmaterial.illegal.Service7;
import scripts.dataexttest.friends.testmaterial.legal.Context6;
import scripts.dataexttest.friends.testmaterial.legal.Service6;
import scripts.dataexttest.friends.testmaterial.legal.ns6_1.DataExt6_1;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class FriendDataExtTest {
    @Test
    public void testAccessFriendDataExt() {
        Service6 service6 = ServiceOrchestrator.getOrchestrator(Service6.class);
        Context6 context6 = new Context6();
        service6.apply(context6);
        DataExt6_1 dataExt6_1 =
                context6.getDataExt("scripts.dataexttest.friends.testmaterial.legal.ns6_1");
        Assertions.assertThat(dataExt6_1.getI()).isEqualTo(4);

        dataExt6_1 =
                context6.getDataExt("scripts.dataexttest.friends.testmaterial.legal.ns6_1");
        Assertions.assertThat(dataExt6_1.getI()).isEqualTo(4);
        Assertions.assertThat(DataExt6_1.getCounter().get()).isEqualTo(1);
    }

    @Test(
        expectedExceptions = RuntimeException.class,
        expectedExceptionsMessageRegExp =
                "Illegal access from provider package .+? to data extension .+?"
    )
    public void testAccessNoFriendDataExt() {
        Service7 service7 = ServiceOrchestrator.getOrchestrator(Service7.class);
        Context7 context7 = new Context7();
        service7.apply(context7);
    }
}
