package com.github.winteryoung.yanwte2demo.models;

import com.github.winteryoung.yanwte2.core.ExtensibleData;

/**
 * @author fanshen
 * @since 2018/1/12
 */
public class AccountTestContext {
    private Account account;
    private Object accountExtension;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Object getAccountExtension() {
        return accountExtension;
    }

    public void setAccountExtension(Object accountExtension) {
        this.accountExtension = accountExtension;
    }
}
