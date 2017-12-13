package com.github.winteryoung.yanwte2.core.internal.combinators;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.testng.annotations.Test;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class ChainCombinatorTest {
    @Test
    public void testShortCircuit() {
        ChainCombinator chain =
                new ChainCombinator(
                        Lists.newArrayList(
                                (a) -> ((int) a) + 2,
                                (a) -> {
                                    throw new RuntimeException("Should not reach here");
                                }));

        Assertions.assertThat(chain.invoke(3)).isEqualTo(5);
    }

    @Test
    public void testNullReturnValue() {
        ChainCombinator chain =
                new ChainCombinator(Lists.newArrayList((a) -> null, (a) -> ((int) a) + 2));

        Assertions.assertThat(chain.invoke(3)).isEqualTo(5);
    }
}
