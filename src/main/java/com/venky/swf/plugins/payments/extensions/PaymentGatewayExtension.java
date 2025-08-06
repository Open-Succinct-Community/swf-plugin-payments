package com.venky.swf.plugins.payments.extensions;

import com.venky.core.util.ObjectUtil;
import com.venky.swf.db.extensions.ModelOperationExtension;
import com.venky.swf.plugins.payments.db.model.payment.gateway.PaymentGateway;
import org.json.JSONObject;

import java.util.StringTokenizer;

public class PaymentGatewayExtension extends ModelOperationExtension<PaymentGateway> {
    static {
        registerExtension(new PaymentGatewayExtension());
    }
    
    @Override
    protected void beforeValidate(PaymentGateway instance) {
        super.beforeValidate(instance);
        if (ObjectUtil.isVoid(instance.getPaymentGatewayCredentials())) {
            JSONObject creds = new JSONObject();
            StringTokenizer tok = new StringTokenizer(instance.getSupportedPaymentGateway().getCredentialAttributes(), ",");
            while (tok.hasMoreTokens()) {
                String token = tok.nextToken();
                creds.put(token, "");
            }
            instance.setPaymentGatewayCredentials(creds.toString());
        }
    }
}
