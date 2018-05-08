package com.github.winteryoung.yanwte2demo.biz.taobao;

import com.github.winteryoung.yanwte2demo.models.Account;
import com.github.winteryoung.yanwte2demo.models.AccountTestContext;
import com.github.winteryoung.yanwte2demo.services.AccountSaveService;

/**
 * @author fanshen
 * @since 2018/5/8
 */
public class TaobaoAccountSaveService implements AccountSaveService {
    @Override
    public Void apply(AccountTestContext context) {
        Account account = context.getAccount();

        Object accountExtension = context.getAccountExtension();
        if (!(accountExtension instanceof TaobaoAccount)) {
            return null;
        }

        TaobaoAccount taobaoAccount = (TaobaoAccount) accountExtension;
        if (account.getAccountId() != taobaoAccount.getAccountId()) {
            throw new RuntimeException("accountId mismatch");
        }

        System.out.println("Saved: " + taobaoAccount);

        return null;
    }
}
