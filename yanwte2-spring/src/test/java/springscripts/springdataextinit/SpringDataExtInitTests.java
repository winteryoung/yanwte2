package springscripts.springdataextinit;

import base.TestBase;
import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import springscripts.springdataextinit.testmaterial.Context3;
import springscripts.springdataextinit.testmaterial.Service3;
import springscripts.springdataextinit.testmaterial.n1.DataExt3_1;

/**
 * @author fanshen
 * @since 2017/12/22
 */
public class SpringDataExtInitTests extends TestBase {
    @Test
    public void test() {
        Service3 service3 = ServiceOrchestrator.getOrchestrator(Service3.class);
        Context3 context = new Context3();
        service3.apply(context);
        DataExt3_1 dataExt3_1 = context.getDataExt("scripts.springdataextinit.testmaterial.n1");
        Assertions.assertThat(dataExt3_1.getStr()).isEqualTo("test-1");
    }
}
