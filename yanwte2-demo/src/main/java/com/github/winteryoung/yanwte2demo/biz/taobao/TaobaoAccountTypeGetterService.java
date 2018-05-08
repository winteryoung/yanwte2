package com.github.winteryoung.yanwte2demo.biz.taobao;

import com.github.winteryoung.yanwte2demo.models.AccountTestContext;
import com.github.winteryoung.yanwte2demo.models.AccountType;
import com.github.winteryoung.yanwte2demo.services.AccountTypeGetterService;

/**
 * @author fanshen
 * @since 2018/5/8
 */
public class TaobaoAccountTypeGetterService implements AccountTypeGetterService {
    @Override
    public AccountType apply(AccountTestContext context) {
        if (context.getAccountExtension() instanceof TaobaoAccount) {
            return AccountType.TAOBAO;
        }
        return null;
    }
}
