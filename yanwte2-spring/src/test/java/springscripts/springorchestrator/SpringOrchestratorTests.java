package springscripts.springorchestrator;

import base.TestBase;
import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import springscripts.springorchestrator.testmaterial.Service1;

/**
 * @author fanshen
 * @since 2017/12/22
 */
public class SpringOrchestratorTests extends TestBase {
    @Test
    public void test() {
        Service1 service1 = ServiceOrchestrator.getOrchestrator(Service1.class);
        String result = service1.apply("test");
        Assertions.assertThat(result).isEqualTo("test-1");
    }
}
