package com.venky.swf.plugins.payments.db.model.payment;

import com.razorpay.RazorpayClient;
import com.venky.core.util.Bucket;
import com.venky.swf.db.annotations.column.COLUMN_DEF;
import com.venky.swf.db.annotations.column.COLUMN_SIZE;
import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.column.defaulting.StandardDefault;
import com.venky.swf.db.annotations.column.pm.PARTICIPANT;
import com.venky.swf.db.annotations.column.validations.Enumeration;
import com.venky.swf.db.model.Model;
import com.venky.swf.plugins.payments.db.model.Application;

import java.io.Reader;
import java.sql.Timestamp;

public interface  Purchase extends Model {
    @PARTICIPANT
    public Long getApplicationId();
    public void setApplicationId(Long id);
    public Application getApplication();

    public Long getPlanId();
    public void setPlanId(Long id);
    public Plan getPlan();

    @UNIQUE_KEY
    public String getRazorpayPaymentId();
    public void setRazorpayPaymentId(String razorpayPaymentId);

    @Enumeration(",created,authorized,captured,refunded,failed")
    @IS_NULLABLE
    public String getStatus();
    public void setStatus(String status);

    @COLUMN_DEF(StandardDefault.BOOLEAN_FALSE)
    public boolean isCaptured();
    public void setCaptured(boolean captured);

    public Reader getPaymentJson();
    public void setPaymentJson(Reader reader);

    public Bucket getRemainingCredits();
    public void setRemainingCredits(Bucket remainingCredits);

    public Timestamp getPurchasedOn();
    public void setPurchasedOn(Timestamp purchasedOn);

    public RazorpayClient createRazorpayClient() ;

    public enum  PaymentStatus {
        created,
        authorized,
        failed,
        captured,
        refunded,
    }

    public boolean canAcceptPayment();

    @COLUMN_SIZE(1024)
    public String getOrderJson();
    public void setOrderJson(String orderJson);

    public String getRazorpayOrderId();
    public void setRazorpayOrderId(String razorpayOrderId);


    @COLUMN_DEF(StandardDefault.BOOLEAN_FALSE)
    @IS_NULLABLE(false)
    public boolean isProduction();
    public void setProduction(boolean production);

}
