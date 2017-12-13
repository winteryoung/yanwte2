package com.github.winteryoung.yanwte2.core.internal.combinators;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.winteryoung.yanwte2.core.spi.Combinator;
import java.util.List;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class ChainCombinator implements Combinator {
    private List<Combinator> combinators;

    public ChainCombinator(List<Combinator> combinators) {
        this.combinators = checkNotNull(combinators);
    }

    @Override
    public Object invoke(Object arg) {
        for (Combinator combinator : combinators) {
            Object combinatorResult = combinator.invoke(arg);
            if (combinatorResult != null) {
                return combinatorResult;
            }
        }
        return null;
    }
}
