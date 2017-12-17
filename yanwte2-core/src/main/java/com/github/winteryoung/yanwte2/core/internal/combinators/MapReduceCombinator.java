package com.github.winteryoung.yanwte2.core.internal.combinators;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * @author Winter Young
 * @since 2017/12/17
 */
public class MapReduceCombinator implements Combinator {
    private List<Combinator> combinators;
    private BinaryOperator<Object> reducer;

    public MapReduceCombinator(
            List<Combinator> combinators, BinaryOperator<Object> reducer) {
        this.reducer = checkNotNull(reducer);

        checkNotNull(combinators);
        this.combinators = ImmutableList.copyOf(combinators);
    }

    @Override
    public Object invoke(Object arg) {
        return combinators.stream()
                .map(c -> c.invoke(arg))
                .reduce(reducer)
                .orElse(null);
    }

    @Override
    public List<Combinator> getChildren() {
        return combinators;
    }

    @Override
    public void setChildren(List<Combinator> children) {
        combinators = ImmutableList.copyOf(children);
    }
}
