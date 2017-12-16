package scripts.serviceorchestrator.testmaterial.lazycombinator;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2.core.spi.Combinator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class Service4Orchestrator implements ServiceOrchestrator<Service4> {
    private static AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Combinator tree() {
        counter.incrementAndGet();
        return (arg) -> null;
    }

    public static AtomicInteger getCounter() {
        return counter;
    }
}
