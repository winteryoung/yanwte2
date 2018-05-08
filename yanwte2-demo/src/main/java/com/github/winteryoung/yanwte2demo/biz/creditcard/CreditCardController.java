package com.github.winteryoung.yanwte2demo.biz.creditcard;

import com.github.winteryoung.yanwte2demo.models.Account;
import com.github.winteryoung.yanwte2demo.services.AccountTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Winter Young
 * @since 2016/10/23
 */
@RestController
public class CreditCardController {
    @Autowired
    private AccountTestService<CreditCard> accountTestService;

    @RequestMapping("/testCreditCard")
    public String testCreditCard(Account account, CreditCard creditCard) {
        accountTestService.testAccount(account, creditCard);
        return "ok";
    }
}
