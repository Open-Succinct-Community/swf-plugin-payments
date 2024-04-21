package com.venky.swf.plugins.payments.db.model.payment;

import com.venky.core.util.Bucket;
import com.venky.swf.db.annotations.column.COLUMN_DEF;
import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.column.defaulting.StandardDefault;
import com.venky.swf.db.annotations.column.validations.Enumeration;
import com.venky.swf.db.annotations.model.MENU;
import com.venky.swf.db.model.Model;
import com.venky.swf.plugins.collab.db.model.CompanyNonSpecific;

import java.sql.Timestamp;

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


}
