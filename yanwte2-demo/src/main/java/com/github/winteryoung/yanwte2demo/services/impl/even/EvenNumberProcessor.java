package com.github.winteryoung.yanwte2demo.services.impl.even;

import com.github.winteryoung.yanwte2demo.services.spi.NumberProcessor;
import org.springframework.stereotype.Service;

/**
 * @author Winter Young
 * @since 2016/10/23
 */
@Service("eventNumberProcessor")
public class EvenNumberProcessor implements NumberProcessor {
    @Override
    public Integer apply(Integer i) {
        if (i != null && i % 2 == 0) {
            return i - 1;
        }

        // we cannot deal with it, let others do the work
        return null;
    }
}
