package com.github.winteryoung.yanwte2.core.internal.utils;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Winter Young
 * @since 2017/12/17
 */
public class LazyTests {
    @Test
    public void testGet_nonNullResult() {
        AtomicInteger counter = new AtomicInteger(0);
        Lazy<Integer> lazy = Lazy.of(() -> {
            counter.incrementAndGet();
            return 3;
        });

        Assertions.assertThat(lazy.get()).isEqualTo(3);
        Assertions.assertThat(lazy.get()).isEqualTo(3);
        Assertions.assertThat(counter.get()).isEqualTo(1);
    }

    @Test
    public void testGet_nullResult() {
        AtomicInteger counter = new AtomicInteger(0);
        Lazy<Integer> lazy = Lazy.of(() -> {
            counter.incrementAndGet();
            return null;
        });

        Assertions.assertThat(lazy.get()).isEqualTo(null);
        Assertions.assertThat(lazy.get()).isEqualTo(null);
        Assertions.assertThat(counter.get()).isEqualTo(1);
    }
}