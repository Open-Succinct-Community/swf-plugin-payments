package com.venky.swf.plugins.payments.db.model.payment.gateway;

import com.venky.swf.db.annotations.column.IS_VIRTUAL;
import com.venky.swf.db.annotations.column.ui.WATERMARK;
import com.venky.swf.db.model.Model;

public interface SupportedPaymentGateway extends Model {
    String getName();
    void setName(String name);
    
    @WATERMARK("Comma separated credential attributes")
    String getCredentialAttributes();
    void setCredentialAttributes(String credentialAttributes);
    
    @IS_VIRTUAL
    String getCredentialTemplate();
    
}
