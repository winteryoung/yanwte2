package com.github.winteryoung.yanwte2.core.spi;

import java.util.List;

/**
 * @author Winter Young
 * @since 2017/12/10
 */
public interface Combinator {
    Object invoke(Object arg);

    List<Combinator> getChildren();

    void setChildren(List<Combinator> children);
}
