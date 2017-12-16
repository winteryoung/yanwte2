package scripts.serviceorchestratortest;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import scripts.serviceorchestratortest.testmaterial.lazycombinator.Service4;
import scripts.serviceorchestratortest.testmaterial.lazycombinator.Service4Orchestrator;
import scripts.serviceorchestratortest.testmaterial.orchestratorwithoutgenerictype.Service5;
import scripts.serviceorchestratortest.testmaterial.simple.*;

/**
 * @author fanshen
 * @since 2017/12/12
 */
public class ServiceOrchestratorTest {
    @Test
    public void getOrchestratorByServiceType_normal() {
        NormalService orchestrator = ServiceOrchestrator.getOrchestrator(NormalService.class);
        Assertions.assertThat(orchestrator.apply(2)).isEqualTo(5);
    }

    @Test(
        expectedExceptions = IllegalStateException.class,
        expectedExceptionsMessageRegExp =
                "Cannot find orchestrator for service: .+?\\.ServiceWithoutOrchestrator"
    )
    public void getOrchestratorByServiceType_noOrchestratorDefined() {
        ServiceOrchestrator.getOrchestrator(ServiceWithoutOrchestrator.class);
    }

    @Test(
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp =
                "Service type is required to be a function: .+?\\.ServiceNotImplementFunction"
    )
    public void getOrchestratorByServiceType_serviceNotImplementFunction() {
        ServiceOrchestrator.getOrchestrator(ServiceNotImplementFunction.class);
    }

    @Test(
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp =
                "Service type is required to be an interface: .+?\\.NormalServiceOrchestrator"
    )
    public void getOrchestratorByServiceType_serviceNotInterface() {
        ServiceOrchestrator.getOrchestrator(NormalServiceOrchestrator.class);
    }

    @Test
    public void testServiceInvocation() {
        SimpleService simpleService = ServiceOrchestrator.getOrchestrator(SimpleService.class);
        String result = simpleService.apply("test");
        Assertions.assertThat(result).isEqualTo("test-abc");
    }

    @Test(
        expectedExceptions = RuntimeException.class,
        expectedExceptionsMessageRegExp =
                "Generic type parameter is required for orchestrator: .+?\\.Service5Orchestrator"
    )
    public void testOrchestratorWithoutGenericType() {
        Service5 service5 = ServiceOrchestrator.getOrchestrator(Service5.class);
        service5.apply("");
    }

    @Test
    public void testLazyCombinatorInvocation() {
        Service4 service4 = ServiceOrchestrator.getOrchestrator(Service4.class);
        service4.apply(null);
        service4.apply(null);
        Assertions.assertThat(Service4Orchestrator.getCounter().get()).isEqualTo(1);
    }
}
