package testmaterial;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.internal.ServiceOutput;
import com.github.winteryoung.yanwte2.core.spi.Combinator;

/**
 * @author fanshen
 * @since 2017/12/11
 */
public class NormalServiceOrchestrator implements ServiceOrchestrator {
    @Override
    public Combinator tree() {
        return input -> {
            int arg = (int) input.getArg();
            return new ServiceOutput(arg + 3);
        };
    }
}
