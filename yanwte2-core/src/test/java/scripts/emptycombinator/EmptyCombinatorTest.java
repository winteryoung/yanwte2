package scripts.emptycombinator;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import scripts.emptycombinator.testmaterial.Service10;
import scripts.emptycombinator.testmaterial.Service12;

/**
 * @author Winter Young
 * @since 2017/12/17
 */
public class EmptyCombinatorTest {
    @Test(
        expectedExceptions = IllegalStateException.class,
        expectedExceptionsMessageRegExp =
                "Combinator is required in tree\\(\\) definition\\. "
                        + "If you don't have one, use empty\\(\\) instead"
    )
    public void testTreeReturningNull() {
        Service10 service10 = ServiceOrchestrator.getOrchestrator(Service10.class);
        service10.apply("");
    }

    @Test
    public void testEmpty() {
        Service12 service12 = ServiceOrchestrator.getOrchestrator(Service12.class);
        Assertions.assertThat(service12.apply("")).isNull();
    }
}
