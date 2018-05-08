package com.github.winteryoung.yanwte2demo.models;

/**
 * @author fanshen
 * @since 2018/5/8
 */
public class Account {
    private Long accountId;
    private AccountType accountType;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "Account{" + "accountId=" + accountId + ", accountType=" + accountType + '}';
    }
}
