package com.venky.swf.plugins.payments.db.model.payment.gateway;

import com.venky.swf.db.annotations.column.COLUMN_SIZE;
import com.venky.swf.db.annotations.column.ENCRYPTED;
import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.indexing.Index;
import com.venky.swf.db.annotations.model.EXPORTABLE;
import com.venky.swf.db.model.Model;
import com.venky.swf.plugins.collab.db.model.CompanySpecific;

public interface PaymentGateway extends Model, CompanySpecific {
    
    @IS_NULLABLE(false)
    @Index
    Long getSupportedPaymentGatewayId();
    void setSupportedPaymentGatewayId(Long id);
    SupportedPaymentGateway getSupportedPaymentGateway();
    
    @COLUMN_SIZE(2048)
    @ENCRYPTED
    @EXPORTABLE(value = false)
    String getPaymentGatewayCredentials();
    void setPaymentGatewayCredentials(String credential);
    
}
