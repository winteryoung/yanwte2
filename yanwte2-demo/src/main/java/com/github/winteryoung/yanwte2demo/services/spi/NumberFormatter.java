package com.github.winteryoung.yanwte2demo.services.spi;

import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.spring.SpringServiceOrchestrator;
import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2016/11/7
 */
public interface NumberFormatter
        extends Function<Integer, String>, SpringServiceOrchestrator<NumberFormatter> {
    @Override
    default Combinator tree() {
        return chain(springProvider("evenNumberFormatter"), springProvider("oddNumberFormatter"));
    }
}
