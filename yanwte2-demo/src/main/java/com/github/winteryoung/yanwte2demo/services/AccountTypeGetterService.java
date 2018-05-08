package com.github.winteryoung.yanwte2demo.services;

import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.spring.SpringServiceOrchestrator;
import com.github.winteryoung.yanwte2demo.biz.creditcard.CreditCardAccountTypeGetterService;
import com.github.winteryoung.yanwte2demo.biz.taobao.TaobaoAccountTypeGetterService;
import com.github.winteryoung.yanwte2demo.models.AccountTestContext;
import com.github.winteryoung.yanwte2demo.models.AccountType;
import java.util.function.Function;

/**
 * @author fanshen
 * @since 2018/5/8
 */
public interface AccountTypeGetterService
        extends Function<AccountTestContext, AccountType>,
                SpringServiceOrchestrator<AccountTypeGetterService> {
    @Override
    default Combinator tree() {
        return chain(
                provider(CreditCardAccountTypeGetterService.class),
                provider(TaobaoAccountTypeGetterService.class));
    }
}
