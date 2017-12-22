package com.github.winteryoung.yanwte2.spring.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Yanwte spring configuration, for auto configuring beans.
 *
 * @author Winter Young
 * @since 2016/10/22
 */
@Configuration
public class Yanwte2SpringConfig {
    @Bean
    public SpringHook yanwteSpringHook() {
        return new SpringHook();
    }
}
