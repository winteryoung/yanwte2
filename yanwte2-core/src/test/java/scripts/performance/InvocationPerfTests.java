package scripts.performance;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import java.time.Duration;
import java.time.LocalTime;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import scripts.performance.testmaterial.Service18;
import scripts.performance.testmaterial.Service18Provider;

/**
 * @author fanshen
 * @since 2017/12/29
 */
public class InvocationPerfTests {
    @Test
    public void test() {
        long base = run(Service18Provider::new, service18 -> service18.apply(null));

        long result =
                run(
                        () -> ServiceOrchestrator.getOrchestrator(Service18.class),
                        service18 -> service18.apply(null));

        Assertions.assertThat(result).isLessThan(base * 15);
    }

    private <T> long run(Supplier<T> serviceSupplier, Consumer<T> serviceConsumer) {
        T service = serviceSupplier.get();
        LocalTime start = LocalTime.now();
        for (int i = 0; i < 100_000_000; i++) {
            serviceConsumer.accept(service);
        }
        LocalTime end = LocalTime.now();
        Duration duration = Duration.between(start, end);
        return duration.toMillis();
    }
}
