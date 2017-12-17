package com.github.winteryoung.yanwte2.core.utils;

import org.assertj.core.api.Assertions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.*;

/**
 * @author Winter Young
 * @since 2017/12/17
 */
public class LazyTest {
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