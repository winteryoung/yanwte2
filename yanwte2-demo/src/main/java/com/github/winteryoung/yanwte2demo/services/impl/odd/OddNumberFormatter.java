package com.github.winteryoung.yanwte2demo.services.impl.odd;

import com.github.winteryoung.yanwte2demo.services.spi.NumberFormatter;
import org.springframework.stereotype.Service;

/**
 * @author Winter Young
 * @since 2016/10/23
 */
@Service("oddNumberFormatter")
public class OddNumberFormatter implements NumberFormatter {
    @Override
    public String apply(Integer num) {
        if (num != null && num % 2 != 0) {
            return "Odd " + num;
        }
        return null;
    }
}
