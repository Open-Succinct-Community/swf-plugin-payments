package com.venky.swf.plugins.payments.extensions;

import com.venky.swf.db.extensions.ModelOperationExtension;
import com.venky.swf.plugins.background.core.TaskManager;
import com.venky.swf.plugins.payments.db.model.payment.gateway.PaymentLink;
import com.venky.swf.plugins.payments.tasks.PaymentStatusEventDispatcher;

public class PaymentLinkExtension extends ModelOperationExtension<PaymentLink> {
    static {
        registerExtension(new PaymentLinkExtension());
    }
    
    @Override
    protected void beforeValidate(PaymentLink instance) {
        super.beforeValidate(instance);
        if (instance.getRawRecord().isFieldDirty("STATUS") ||instance.getRawRecord().isFieldDirty("ACTIVE")){
            instance.setStatusCommunicated(false);
            if (!instance.getRawRecord().isNewRecord()) {
                TaskManager.instance().executeAsync(new PaymentStatusEventDispatcher(instance.getId()));
            }
        }
    }
    
}
