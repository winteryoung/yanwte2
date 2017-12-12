package com.github.winteryoung.yanwte2.core.spi;

import com.github.winteryoung.yanwte2.core.internal.ServiceOutput;
import com.github.winteryoung.yanwte2.core.internal.ServiceInput;

/**
 * @author Winter Young
 * @since 2017/12/10
 */
public interface Combinator {
    ServiceOutput invoke(ServiceInput input);
}
