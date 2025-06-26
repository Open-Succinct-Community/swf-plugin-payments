package com.venky.swf.plugins.payments.tasks;

import com.venky.swf.plugins.background.core.Task;
import com.venky.swf.plugins.payments.db.model.payment.gateway.PaymentGateway;
import com.venky.swf.plugins.payments.gateway.PaymentGatewayAdaptor;
import com.venky.swf.plugins.payments.gateway.PaymentGatewayAdaptorFactory;

import java.util.Map;

public class PaymentLinkStatusUpdate implements Task {
    String payload;
    Map<String,String> headers ;
    PaymentGateway paymentGateway;
    public PaymentLinkStatusUpdate(String payload, Map<String,String> headers, PaymentGateway paymentGateway){
        this.payload = payload;
        this.headers = headers;
        this.paymentGateway = paymentGateway;
        
    }
    @Override
    public void execute() {
        PaymentGatewayAdaptor adaptor  =PaymentGatewayAdaptorFactory.getInstance().create(paymentGateway);
        adaptor.recordPayment(payload,headers);
    }
}
