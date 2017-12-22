package com.github.winteryoung.yanwte2.core.internal;

import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.core.spi.LeafCombinator;

import java.util.List;

/**
 * @author Winter Young
 * @since 2017/12/17
 */
public interface SurrogateCombinator extends LeafCombinator {
    @Override
    default Object invoke(Object arg) {
        return null;
    }

    List<Combinator> getSurrogateCombinators();
}
