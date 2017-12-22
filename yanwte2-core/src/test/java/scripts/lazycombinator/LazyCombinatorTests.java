package scripts.lazycombinator;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import scripts.lazycombinator.testmaterial.Service4;
import scripts.lazycombinator.testmaterial.Service4Orchestrator;

/**
 * @author Winter Young
 * @since 2017/12/17
 */
public class LazyCombinatorTests {
    @Test
    public void testLazyCombinatorInvocation() {
        Service4 service4 = ServiceOrchestrator.getOrchestrator(Service4.class);
        service4.apply(null);
        service4.apply(null);
        Assertions.assertThat(Service4Orchestrator.getCounter().get()).isEqualTo(1);
    }
}
