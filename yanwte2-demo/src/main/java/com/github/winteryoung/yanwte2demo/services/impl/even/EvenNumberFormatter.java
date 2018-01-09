package com.github.winteryoung.yanwte2demo.services.impl.even;

import com.github.winteryoung.yanwte2demo.services.spi.NumberFormatter;
import org.springframework.stereotype.Service;

/**
 * @author Winter Young
 * @since 2016/10/23
 */
@Service("evenNumberFormatter")
public class EvenNumberFormatter implements NumberFormatter {
    @Override
    public String apply(Integer num) {
        if (num != null && num % 2 == 0) {
            return "Even " + num + "\n";
        }
        return null;
    }
}
