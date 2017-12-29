package com.github.winteryoung.yanwte2.core.listeners;

import java.util.function.Function;

/**
 * @author fanshen
 * @since 2017/12/29
 */
public interface InvocationListener {
    void onInvocation(Object parameter, Object result);
}
