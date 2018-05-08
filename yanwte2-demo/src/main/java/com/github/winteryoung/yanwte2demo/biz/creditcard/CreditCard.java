package com.github.winteryoung.yanwte2demo.biz.creditcard;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author fanshen
 * @since 2018/5/8
 */
public class CreditCard {
    private String creditCardNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;

    private long accountId;

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "CreditCard{"
                + "creditCardNo='"
                + creditCardNo
                + '\''
                + ", expirationDate="
                + expirationDate
                + ", accountId="
                + accountId
                + '}';
    }
}
