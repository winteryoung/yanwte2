package scripts.providercombinator;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import org.testng.annotations.Test;
import scripts.providercombinator.testmaterial.Service11;

/**
 * @author Winter Young
 * @since 2017/12/17
 */
public class ProviderCombinatorTests {
    @Test(
        expectedExceptions = IllegalArgumentException.class,
        expectedExceptionsMessageRegExp = "Multiple entries with same key.+"
    )
    public void testLoadingTwoProvidersFromSamePackage() {
        Service11 service11 = ServiceOrchestrator.getOrchestrator(Service11.class);
        service11.apply("");
    }
}
