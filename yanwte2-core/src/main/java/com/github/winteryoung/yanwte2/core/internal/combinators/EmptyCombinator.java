package com.github.winteryoung.yanwte2.core.internal.combinators;

import com.github.winteryoung.yanwte2.core.spi.LeafCombinator;

/**
 * @author Winter Young
 * @since 2017/12/17
 */
public class EmptyCombinator implements LeafCombinator {
    @Override
    public Object invoke(Object arg) {
        return null;
    }
}
