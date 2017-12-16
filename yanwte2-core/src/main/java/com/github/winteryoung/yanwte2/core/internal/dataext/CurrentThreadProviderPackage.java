package com.github.winteryoung.yanwte2.core.internal.dataext;

/**
 * @author Winter Young
 * @since 2017/12/13
 */
public class CurrentThreadProviderPackage {
    private CurrentThreadProviderPackage() {}

    private static ThreadLocal<String> cache = new ThreadLocal<>();

    public static String get() {
        return cache.get();
    }

    public static void set(String providerPackage) {
        cache.set(providerPackage);
    }
}
