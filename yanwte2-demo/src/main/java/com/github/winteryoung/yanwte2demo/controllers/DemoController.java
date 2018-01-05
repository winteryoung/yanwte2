package com.github.winteryoung.yanwte2demo.controllers;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2demo.services.spi.NumberFormatter;
import com.github.winteryoung.yanwte2demo.services.spi.NumberProcessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Winter Young
 * @since 2016/10/23
 */
@RestController
public class DemoController {
    private NumberProcessor numberProcessor =
            ServiceOrchestrator.getOrchestrator(NumberProcessor.class);

    private NumberFormatter numberFormatter =
            ServiceOrchestrator.getOrchestrator(NumberFormatter.class);

    @RequestMapping("/demo")
    public String demo(Integer num) {
        Integer i = numberProcessor.apply(num);
        return numberFormatter.apply(i);
    }
}
