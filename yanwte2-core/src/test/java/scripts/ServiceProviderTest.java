package scripts;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import testmaterial.SimpleService;

/**
 * @author fanshen
 * @since 2017/12/12
 */
public class ServiceProviderTest {
    @Test
    public void testInvoke() {
        SimpleService simpleService =
                ServiceOrchestrator.getOrchestratorByServiceType(SimpleService.class);
        String result = simpleService.apply("test");
        Assertions.assertThat(result).isEqualTo("test-abc");
    }
}
