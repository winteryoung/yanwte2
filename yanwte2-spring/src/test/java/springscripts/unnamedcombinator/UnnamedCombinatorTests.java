package springscripts.unnamedcombinator;

import base.TestBase;
import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import springscripts.unnamedcombinator.testmaterial.Service2;

/**
 * @author fanshen
 * @since 2017/12/22
 */
public class UnnamedCombinatorTests extends TestBase {
    @Test
    public void test() {
        Service2 service2 = ServiceOrchestrator.getOrchestrator(Service2.class);
        String result = service2.apply("test");
        Assertions.assertThat(result).isEqualTo("test-1-2");
    }
}
