package com.venky.swf.plugins.payments.db.model.payment.gateway;

import com.venky.swf.db.annotations.column.COLUMN_DEF;
import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.column.defaulting.StandardDefault;
import com.venky.swf.db.annotations.column.validations.Enumeration;
import com.venky.swf.db.model.Model;
import com.venky.swf.plugins.collab.db.model.participants.Application;

public interface PaymentLink extends Model {
    @IS_NULLABLE(false)
    @UNIQUE_KEY("K1")
    String getLinkUri();
    void setLinkUri(String linkUri);
    
    
    
    String getTxnReference();
    void setTxnReference(String txnReference);
    
    @COLUMN_DEF(StandardDefault.BOOLEAN_TRUE)
    boolean isActive();
    void setActive(boolean active);
    
    @Enumeration(enumClass = "in.succinct.beckn.Payment$PaymentStatus")
    String getStatus();
    void setStatus(String status);
    
    @COLUMN_DEF(StandardDefault.BOOLEAN_FALSE)
    boolean isStatusCommunicated();
    void setStatusCommunicated(boolean statusCommunicated);
    
    
    Long getApplicationId();
    void setApplicationId(Long id);
    Application getApplication();
    
    @COLUMN_DEF(StandardDefault.ZERO)
    double getAmountPaid();
    void setAmountPaid(double amountPaid);
    
    
    
}
