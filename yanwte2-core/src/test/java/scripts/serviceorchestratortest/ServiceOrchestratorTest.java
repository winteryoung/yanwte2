package scripts.serviceorchestratortest;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import scripts.serviceorchestratortest.testmaterial.*;

/**
 * @author fanshen
 * @since 2017/12/12
 */
public class ServiceOrchestratorTest {
    @Test
    public void getOrchestratorByServiceType_normal() {
        NormalService orchestrator =
                ServiceOrchestrator.getOrchestrator(NormalService.class);
        Assertions.assertThat(orchestrator.apply(2)).isEqualTo(5);
    }

    @Test(
        expectedExceptions = IllegalStateException.class,
        expectedExceptionsMessageRegExp =
                "Cannot find orchestrator for service: "
                        + "scripts.serviceorchestratortest.testmaterial."
                        + "ServiceWithoutOrchestrator"
    )
    public void getOrchestratorByServiceType_noOrchestratorDefined() {
        ServiceOrchestrator.getOrchestrator(ServiceWithoutOrchestrator.class);
    }

    @Test(
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp =
                "Service type is required to be a function: "
                        + "scripts.serviceorchestratortest.testmaterial."
                        + "ServiceNotImplementFunction"
    )
    public void getOrchestratorByServiceType_serviceNotImplementFunction() {
        ServiceOrchestrator.getOrchestrator(ServiceNotImplementFunction.class);
    }

    @Test(
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp =
                "Service type is required to be an interface: "
                        + "scripts.serviceorchestratortest.testmaterial."
                        + "NormalServiceOrchestrator"
    )
    public void getOrchestratorByServiceType_serviceNotInterface() {
        ServiceOrchestrator.getOrchestrator(NormalServiceOrchestrator.class);
    }

    @Test
    public void testServiceInvocation() {
        SimpleService simpleService =
                ServiceOrchestrator.getOrchestrator(SimpleService.class);
        String result = simpleService.apply("test");
        Assertions.assertThat(result).isEqualTo("test-abc");
    }
}
