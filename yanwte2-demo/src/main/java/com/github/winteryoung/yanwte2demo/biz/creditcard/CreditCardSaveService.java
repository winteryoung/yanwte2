package com.github.winteryoung.yanwte2demo.biz.creditcard;

import com.github.winteryoung.yanwte2demo.models.Account;
import com.github.winteryoung.yanwte2demo.models.AccountTestContext;
import com.github.winteryoung.yanwte2demo.services.AccountSaveService;

/**
 * @author fanshen
 * @since 2018/5/8
 */
public class CreditCardSaveService implements AccountSaveService {
    @Override
    public Void apply(AccountTestContext context) {
        Account account = context.getAccount();

        Object accountExtension = context.getAccountExtension();
        if (!(accountExtension instanceof CreditCard)) {
            return null;
        }

        CreditCard creditCard = (CreditCard) accountExtension;
        if (account.getAccountId() != creditCard.getAccountId()) {
            throw new RuntimeException("accountId mismatch");
        }

        System.out.println("Saved: " + creditCard);

        return null;
    }
}
