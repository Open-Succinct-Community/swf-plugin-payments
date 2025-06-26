package com.venky.swf.plugins.payments.db.model.payment.gateway;

import com.venky.core.string.StringUtil;
import com.venky.swf.db.table.ModelImpl;
import org.json.JSONObject;

import java.util.StringTokenizer;

public class SupportedPaymentGatewayImpl extends ModelImpl<SupportedPaymentGateway> {
    public SupportedPaymentGatewayImpl() {
    
    }
    
    public SupportedPaymentGatewayImpl(SupportedPaymentGateway proxy) {
        super(proxy);
    }
    
    public String getCredentialTemplate() {
        SupportedPaymentGateway instance = getProxy();
        
        StringTokenizer tokenizer = new StringTokenizer(StringUtil.valueOf(instance.getCredentialAttributes()),",");
        JSONObject object = new JSONObject();
        while (tokenizer.hasMoreTokens()) {
            object.put(tokenizer.nextToken(), "");
        }
        return object.toString();
    }
}
