package com.github.winteryoung.yanwte2.core.internal;

import java.util.function.Function;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

public class OrchestratorInterceptor {
    private Function<Object, Object> function;

    @SuppressWarnings("WeakerAccess")
    public OrchestratorInterceptor(Function<Object, Object> function) {
        this.function = function;
    }

    @SuppressWarnings("unused")
    @RuntimeType
    public Object intercept(@AllArguments Object[] allArguments) {
        Object arg = allArguments[0];
        return function.apply(arg);
    }
}
