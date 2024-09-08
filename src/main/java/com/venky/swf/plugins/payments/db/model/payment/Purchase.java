package com.venky.swf.plugins.payments.db.model.payment;

import com.venky.core.date.DateUtils;
import com.venky.core.util.Bucket;
import com.venky.swf.db.JdbcTypeHelper.TypeConverter;
import com.venky.swf.db.annotations.column.COLUMN_DEF;
import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.IS_VIRTUAL;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.column.defaulting.StandardDefault;
import com.venky.swf.db.annotations.column.validations.Enumeration;
import com.venky.swf.db.annotations.model.MENU;
import com.venky.swf.db.model.Model;
import com.venky.swf.db.model.User;
import com.venky.swf.plugins.collab.db.model.CompanyNonSpecific;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

@MENU("Platform Revenue")
public interface Purchase extends Model, CompanyNonSpecific {
    @IS_NULLABLE(false)
    @UNIQUE_KEY
    public Long getPlanId();
    public void setPlanId(Long id);
    public Plan getPlan();


    @Enumeration(enumClass = "com.venky.swf.plugins.payments.db.model.payment.Purchase$PaymentStatus")
    @IS_NULLABLE
    public String getStatus();
    public void setStatus(String status);


    public Bucket getRemainingCredits();
    public void setRemainingCredits(Bucket remainingCredits);

    public Timestamp getPurchasedOn();
    public void setPurchasedOn(Timestamp purchasedOn);

    @UNIQUE_KEY
    public String getPaymentReference();
    public void setPaymentReference(String paymentReference);

    public Long getPaymentMadeById();
    public void setPaymentMadeById(Long id);
    public User getPaymentMadeBy();

    @COLUMN_DEF(StandardDefault.BOOLEAN_FALSE)
    public boolean isCaptured();
    public void setCaptured(boolean captured);


    public enum  PaymentStatus {
        created,
        authorized,
        failed,
        captured,
        refunded,
    }

    Date getEffectiveFrom();
    void setEffectiveFrom(Date effectiveFrom);


    Date getExpiresOn();
    void setExpiresOn(Date expiresOn);



}
