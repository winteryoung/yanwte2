package com.github.winteryoung.yanwte2demo.biz.taobao;

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
public class TaobaoAccountController {
    @Autowired private AccountTestService<TaobaoAccount> accountTestService;

    @RequestMapping("/testTaobaoAccount")
    public String testDebitCard(Account account, TaobaoAccount taobaoAccount) {
        accountTestService.testAccount(account, taobaoAccount);
        return "ok";
    }
}
