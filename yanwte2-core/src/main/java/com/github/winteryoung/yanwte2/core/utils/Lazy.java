package com.github.winteryoung.yanwte2.core.utils;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.function.Supplier;

/**
 * @author Winter Young
 * @since 2017/12/16
 */
public class Lazy<T> {
    private static final Object NULL_VALUE = new Object();

    private Object value;
    private Supplier<T> supplier;

    private Lazy(Supplier<T> supplier) {
        this.supplier = checkNotNull(supplier);
    }

    @SuppressWarnings("unchecked")
    public synchronized T get() {
        if (value == NULL_VALUE) {
            return null;
        }

        if (value != null) {
            return (T) value;
        }

        value = supplier.get();
        if (value == null) {
            value = NULL_VALUE;
        }

        return get();
    }

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }
}
