package com.github.winteryoung.yanwte2.core.spi;

import com.google.common.collect.ImmutableList;
import java.util.List;

/**
 * @author Winter Young
 * @since 2017/12/17
 */
public interface LeafCombinator extends Combinator {
    @Override
    default List<Combinator> getChildren() {
        return ImmutableList.of();
    }

    @Override
    default void setChildren(List<Combinator> children) {}

    @Override
    default String getName() {
        return "dynamic";
    }
}
