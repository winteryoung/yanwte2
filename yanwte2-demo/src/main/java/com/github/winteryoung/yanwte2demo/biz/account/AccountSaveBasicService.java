package com.github.winteryoung.yanwte2demo.biz.account;

import com.github.winteryoung.yanwte2demo.models.Account;
import com.github.winteryoung.yanwte2demo.models.AccountTestContext;
import com.github.winteryoung.yanwte2demo.services.AccountSaveService;

/**
 * @author fanshen
 * @since 2018/5/8
 */
public class AccountSaveBasicService implements AccountSaveService {
    @Override
    public Void apply(AccountTestContext accountTestContext) {
        System.out.println("Saved: " + accountTestContext.getAccount());
        return null;
    }
}
