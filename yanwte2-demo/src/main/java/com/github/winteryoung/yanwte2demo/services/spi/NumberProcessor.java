package com.github.winteryoung.yanwte2demo.services.spi;

import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.spring.SpringServiceOrchestrator;
import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2016/10/23
 */
public interface NumberProcessor
        extends Function<Integer, Integer>, SpringServiceOrchestrator<NumberProcessor> {
    @Override
    default Combinator tree() {
        return chain(springProvider("oddNumberProcessor"), springProvider("eventNumberProcessor"));
    }
}
