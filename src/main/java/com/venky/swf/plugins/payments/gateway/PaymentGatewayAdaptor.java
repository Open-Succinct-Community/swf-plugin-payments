package com.venky.swf.plugins.payments.gateway;

import com.venky.core.string.StringUtil;
import com.venky.core.util.ObjectUtil;
import com.venky.swf.plugins.payments.db.model.payment.gateway.PaymentGateway;
import in.succinct.beckn.Request;
import in.succinct.json.JSONAwareWrapper;
import org.json.simple.JSONObject;


import java.util.Map;

public abstract class PaymentGatewayAdaptor {
    protected final PaymentGateway paymentGateway;
    JSONObject creds = null;
    protected PaymentGatewayAdaptor(PaymentGateway paymentGateway){
        this.paymentGateway = paymentGateway;
        String paymentGatewayCredentials =  paymentGateway.getPaymentGatewayCredentials();
        this.creds = ObjectUtil.isVoid(paymentGatewayCredentials)? new JSONObject() : JSONAwareWrapper.parse(paymentGatewayCredentials);
    }
    public String getCred(String key){
        return StringUtil.valueOf(creds.get(key));
    }
    
    
    public abstract void  createPaymentLink(Request request);
    public abstract void recordPayment(String payload, Map<String,String> headers);
}
