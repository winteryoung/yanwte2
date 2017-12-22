package scripts.unnamedcombinator;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import scripts.unnamedcombinator.testmaterial.service8.Service8;
import scripts.unnamedcombinator.testmaterial.service9.Service9;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class UnnamedCombinatorTests {
    @Test
    public void testUnnamedCombinator_noNamedCombinator() {
        Service8 service8 = ServiceOrchestrator.getOrchestrator(Service8.class);
        String result = service8.apply("test");
        Assertions.assertThat(result).isEqualTo("test-1-2");
    }

    @Test
    public void testUnnamedCombinator_hasNamedCombinator() {
        Service9 service9 = ServiceOrchestrator.getOrchestrator(Service9.class);
        String result = service9.apply("test");
        Assertions.assertThat(result).isEqualTo("test-1-2");
    }
}
