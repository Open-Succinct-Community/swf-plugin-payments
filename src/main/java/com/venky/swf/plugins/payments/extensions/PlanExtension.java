package com.venky.swf.plugins.payments.extensions;

import com.venky.core.math.DoubleUtils;
import com.venky.swf.db.extensions.ModelOperationExtension;
import com.venky.swf.plugins.payments.db.model.payment.Plan;

public class PlanExtension extends ModelOperationExtension<Plan> {
    static {
        registerExtension(new PlanExtension());
    }
    
    @Override
    protected void beforeValidate(Plan instance) {
        super.beforeValidate(instance);
        if (instance.isWalletTopUp()){
            if (!DoubleUtils.equals(0,instance.getTaxPercentage())){
                throw new RuntimeException("Tax Percent is determined only when products are purchased with wallet credits.");
            }
            if (!DoubleUtils.equals(0,instance.getDiscountPercentage())){
                throw new RuntimeException("Discounts are determined only when products purchased with wallet credits.");
            }
        }
    }
}
