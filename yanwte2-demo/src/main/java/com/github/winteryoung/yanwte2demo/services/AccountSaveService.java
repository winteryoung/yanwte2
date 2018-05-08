package com.github.winteryoung.yanwte2demo.services;

import com.github.winteryoung.yanwte2.core.spi.Combinator;
import com.github.winteryoung.yanwte2.spring.SpringServiceOrchestrator;
import com.github.winteryoung.yanwte2demo.biz.account.AccountSaveBasicService;
import com.github.winteryoung.yanwte2demo.biz.creditcard.CreditCardSaveService;
import com.github.winteryoung.yanwte2demo.biz.taobao.TaobaoAccountSaveService;
import com.github.winteryoung.yanwte2demo.models.AccountTestContext;
import java.util.function.Function;

/**
 * @author fanshen
 * @since 2018/5/8
 */
public interface AccountSaveService
        extends Function<AccountTestContext, Void>, SpringServiceOrchestrator<AccountSaveService> {
    @Override
    default Combinator tree() {
        return chain(
                provider(AccountSaveBasicService.class),
                chain(
                        provider(CreditCardSaveService.class),
                        provider(TaobaoAccountSaveService.class)));
    }
}
