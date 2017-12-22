package scripts.dataext.initdataext;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import scripts.dataext.initdataext.testmaterial.Context;
import scripts.dataext.initdataext.testmaterial.multicontexts.Context15;
import scripts.dataext.initdataext.testmaterial.multicontexts.Context16;
import scripts.dataext.initdataext.testmaterial.multicontexts.Service15;
import scripts.dataext.initdataext.testmaterial.multicontexts.Service16;
import scripts.dataext.initdataext.testmaterial.multiservices.MultiService1;
import scripts.dataext.initdataext.testmaterial.multiservices.MultiService2;
import scripts.dataext.initdataext.testmaterial.multiservices.n1.DataExtN1;
import scripts.dataext.initdataext.testmaterial.multiservices.n2.DataExtN2;
import scripts.dataext.initdataext.testmaterial.simple.Service1;
import scripts.dataext.initdataext.testmaterial.simple.Service2;
import scripts.dataext.initdataext.testmaterial.simple.Service3;
import scripts.dataext.initdataext.testmaterial.simple.ns1.DataExtInitializerImplNs1;
import scripts.dataext.initdataext.testmaterial.simple.ns2.DataExtInitializerImplNs2;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class InitDataExtTests {
    @Test
    public void testDataExtensionInitializer_nonNullDataExt() {
        Context context = new Context();

        Service1 service1 = ServiceOrchestrator.getOrchestrator(Service1.class);
        Integer service1Result = service1.apply(context);
        Assertions.assertThat(service1Result).isEqualTo(3);

        service1.apply(context);
        Assertions.assertThat(DataExtInitializerImplNs1.getCounter()).isEqualTo(1);
    }

    @Test
    public void testDataExtensionInitializer_nullDataExt_nonNullInitializer() {
        Context context = new Context();

        Service2 service2 = ServiceOrchestrator.getOrchestrator(Service2.class);
        Integer service2Result = service2.apply(context);
        Assertions.assertThat(service2Result).isNull();

        service2.apply(context);
        Assertions.assertThat(DataExtInitializerImplNs2.getCounter()).isEqualTo(1);
    }

    @Test
    public void testDataExtensionInitializer_nullDataExt_nullInitializer() {
        Context context = new Context();

        Service3 service3 = ServiceOrchestrator.getOrchestrator(Service3.class);
        Integer service3Result = service3.apply(context);
        Assertions.assertThat(service3Result).isNull();
    }

    @Test
    public void testDataExtensionInitializer_multiServices() {
        Context context = new Context();

        MultiService1 service1 = ServiceOrchestrator.getOrchestrator(MultiService1.class);
        service1.apply(context);
        DataExtN1 dataExtN1 =
                context.getDataExt("scripts.dataext.initdataext.testmaterial.multiservices.n1");
        Assertions.assertThat(dataExtN1.getI()).isEqualTo(4);
        DataExtN2 dataExtN2 =
                context.getDataExt("scripts.dataext.initdataext.testmaterial.multiservices.n2");
        Assertions.assertThat(dataExtN2.getI()).isEqualTo(10);

        MultiService2 service2 = ServiceOrchestrator.getOrchestrator(MultiService2.class);
        service2.apply(context);
        dataExtN1 = context.getDataExt("scripts.dataext.initdataext.testmaterial.multiservices.n1");
        Assertions.assertThat(dataExtN1.getI()).isEqualTo(5);
        dataExtN2 = context.getDataExt("scripts.dataext.initdataext.testmaterial.multiservices.n2");
        Assertions.assertThat(dataExtN2.getI()).isEqualTo(11);
    }

    @Test
    public void testDataExtensionInitializer_multiInitializersInOneProviderPackage() {
        Service15 service15 = ServiceOrchestrator.getOrchestrator(Service15.class);
        String result15 = service15.apply(new Context15());
        Assertions.assertThat(result15).isEqualTo("15");

        Service16 service16 = ServiceOrchestrator.getOrchestrator(Service16.class);
        String result16 = service16.apply(new Context16());
        Assertions.assertThat(result16).isEqualTo("16");
    }
}
