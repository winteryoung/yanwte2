package scripts.dataexttest.setdataext;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import scripts.dataexttest.setdataext.testmaterial.Context;
import scripts.dataexttest.setdataext.testmaterial.Service1;
import scripts.dataexttest.setdataext.testmaterial.Service2;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class SetDataExtTest {
    @Test
    public void testGettingDataExtension() {
        Context context = new Context();

        Service1 service1 = ServiceOrchestrator.getOrchestrator(Service1.class);
        service1.apply(context);

        Service2 service2 = ServiceOrchestrator.getOrchestrator(Service2.class);
        Integer service2Result = service2.apply(context);
        Assertions.assertThat(service2Result).isEqualTo(3);
    }
}
