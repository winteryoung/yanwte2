package com.github.winteryoung.yanwte2demo.services;

import com.github.winteryoung.yanwte2.core.ServiceOrchestrator;
import com.github.winteryoung.yanwte2demo.models.Account;
import com.github.winteryoung.yanwte2demo.models.AccountTestContext;
import com.github.winteryoung.yanwte2demo.models.AccountType;
import org.springframework.stereotype.Service;

/**
 * @author fanshen
 * @since 2018/5/8
 */
@Service
public class AccountTestService<T> {
    private AccountTypeGetterService accountTypeGetterService =
            ServiceOrchestrator.getOrchestrator(AccountTypeGetterService.class);

    private AccountSaveService accountSaveService =
            ServiceOrchestrator.getOrchestrator(AccountSaveService.class);

    public void testAccount(Account account, T t) {
        AccountTestContext context = new AccountTestContext();
        context.setAccount(account);
        context.setAccountExtension(t);

        AccountType accountType = accountTypeGetterService.apply(context);
        account.setAccountType(accountType);
        System.out.println("accountType: " + accountType);

        accountSaveService.apply(context);
    }
}
