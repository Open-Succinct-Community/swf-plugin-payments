package com.venky.swf.plugins.payments.gateway;

import com.venky.swf.plugins.payments.db.model.payment.gateway.PaymentGateway;

import java.util.HashMap;
import java.util.Map;

public class PaymentGatewayAdaptorFactory {
    private static volatile PaymentGatewayAdaptorFactory sSoleInstance;
    
    //private constructor.
    private PaymentGatewayAdaptorFactory() {
        //Prevent form the reflection api.
        if (sSoleInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }
    
    public static PaymentGatewayAdaptorFactory getInstance() {
        if (sSoleInstance == null) { //if there is no instance available... create new one
            synchronized (PaymentGatewayAdaptorFactory.class) {
                if (sSoleInstance == null)
                    sSoleInstance = new PaymentGatewayAdaptorFactory();
            }
        }
        
        return sSoleInstance;
    }
    
    //Make singleton from serialize and deserialize operation.
    protected PaymentGatewayAdaptorFactory readResolve() {
        return getInstance();
    }
    
    private final Map<String,Class<? extends PaymentGatewayAdaptor>> adaptorMap = new HashMap<>();
    public void register(String name,Class<? extends PaymentGatewayAdaptor> adaptorClass){
        adaptorMap.put(name,adaptorClass);
    }
    public PaymentGatewayAdaptor create(PaymentGateway paymentGateway){
        
        if (paymentGateway != null) {
            Class<? extends PaymentGatewayAdaptor> adaptorClass = adaptorMap.get(paymentGateway.getSupportedPaymentGateway().getName());
            try {
                return adaptorClass.getConstructor(PaymentGateway.class).newInstance(paymentGateway);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        
        return null;
    }
}
