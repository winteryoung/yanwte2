package scripts.providernstest.initdataext;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import scripts.providernstest.initdataext.testmaterial.Context;
import scripts.providernstest.initdataext.testmaterial.Service1;
import scripts.providernstest.initdataext.testmaterial.Service2;
import scripts.providernstest.initdataext.testmaterial.Service3;
import scripts.providernstest.initdataext.testmaterial.ns1.DataExtInitializerImplNs1;
import scripts.providernstest.initdataext.testmaterial.ns2.DataExtInitializerImplNs2;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class InitDataExtTest {
    @Test
    public void testDataExtensionInitializer_nonNullDataExt() {
        Context context = new Context();

        Service1 service1 = ServiceOrchestrator.getOrchestrator(Service1.class);
        Integer service2Result = service1.apply(context);
        Assertions.assertThat(service2Result).isEqualTo(3);

        Assertions.assertThat(DataExtInitializerImplNs1.getCounter()).isEqualTo(1);
    }

    @Test
    public void testDataExtensionInitializer_nullDataExt_nonNullInitializer() {
        Context context = new Context();

        Service2 service2 = ServiceOrchestrator.getOrchestrator(Service2.class);
        Integer service2Result = service2.apply(context);
        Assertions.assertThat(service2Result).isNull();

        Assertions.assertThat(DataExtInitializerImplNs2.getCounter()).isEqualTo(1);
    }

    @Test
    public void testDataExtensionInitializer_nullDataExt_nullInitializer() {
        Context context = new Context();

        Service3 service3 = ServiceOrchestrator.getOrchestrator(Service3.class);
        Integer service3Result = service3.apply(context);
        Assertions.assertThat(service3Result).isNull();
    }
}
