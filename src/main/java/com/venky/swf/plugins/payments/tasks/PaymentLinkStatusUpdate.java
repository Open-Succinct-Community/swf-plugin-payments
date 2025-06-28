package com.venky.swf.plugins.payments.tasks;

import com.venky.swf.db.Database;
import com.venky.swf.plugins.background.core.Task;
import com.venky.swf.plugins.payments.db.model.payment.gateway.PaymentGateway;
import com.venky.swf.plugins.payments.gateway.PaymentGatewayAdaptor;
import com.venky.swf.plugins.payments.gateway.PaymentGatewayAdaptorFactory;
import com.venky.swf.routing.Config;

import java.util.Map;

public class PaymentLinkStatusUpdate implements Task {
    String payload;
    Map<String,String> headers ;
    long paymentGatewayId ;
    String hostName ;
    public PaymentLinkStatusUpdate(){
    
    }
    public PaymentLinkStatusUpdate(String payload, Map<String,String> headers, PaymentGateway paymentGateway){
        this.payload = payload;
        this.headers = headers;
        this.paymentGatewayId = paymentGateway.getId();
        this.hostName = Config.instance().getHostName();
    }
    @Override
    public void execute() {
        Config.instance().setHostName(this.hostName);
        PaymentGateway paymentGateway = Database.getTable(PaymentGateway.class).get(paymentGatewayId);
        if (paymentGateway != null) {
            PaymentGatewayAdaptor adaptor = PaymentGatewayAdaptorFactory.getInstance().create(paymentGateway);
            adaptor.recordPayment(payload, headers);
        }
    }
}
