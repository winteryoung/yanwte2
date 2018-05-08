package com.github.winteryoung.yanwte2demo.biz.creditcard;

import com.github.winteryoung.yanwte2demo.models.AccountTestContext;
import com.github.winteryoung.yanwte2demo.models.AccountType;
import com.github.winteryoung.yanwte2demo.services.AccountTypeGetterService;

/**
 * @author fanshen
 * @since 2018/5/8
 */
public class CreditCardAccountTypeGetterService implements AccountTypeGetterService {
    @Override
    public AccountType apply(AccountTestContext context) {
        if (context.getAccountExtension() instanceof CreditCard) {
            return AccountType.CREDIT_CARD;
        }
        return null;
    }
}
