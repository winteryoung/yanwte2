package com.github.winteryoung.yanwte2demo.biz.taobao;

/**
 * @author fanshen
 * @since 2018/5/8
 */
public class TaobaoAccount {
    private String taobaoAccountId;
    private long accountId;

    public String getTaobaoAccountId() {
        return taobaoAccountId;
    }

    public void setTaobaoAccountId(String taobaoAccountId) {
        this.taobaoAccountId = taobaoAccountId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "TaobaoAccount{" +
                "taobaoAccountId='" + taobaoAccountId + '\'' +
                ", accountId=" + accountId +
                '}';
    }
}
