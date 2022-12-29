package com.venky.swf.plugins.payments.extensions;

import com.venky.swf.db.Database;
import com.venky.swf.db.extensions.BeforeModelValidateExtension;
import com.venky.swf.plugins.background.core.TaskManager;
import com.venky.swf.plugins.payments.controller.PurchasesController.PaymentCapture;
import com.venky.swf.plugins.payments.db.model.payment.Purchase;

import java.sql.Timestamp;

public class BeforeValidatePurchase extends BeforeModelValidateExtension<Purchase> {
    static {
        registerExtension(new BeforeValidatePurchase());
    }
    @Override
    public void beforeValidate(Purchase model) {
        if (model.getRawRecord().isFieldDirty("REMAINING_CREDITS") ){
            Double oldValue = (Double) model.getRawRecord().getOldValue("REMAINING_CREDITS");
            double currValue = model.getRemainingCredits().doubleValue();
            if (model.getReflector().getJdbcTypeHelper().getTypeRef(double.class).getTypeConverter().valueOf(oldValue) < currValue ) {
                throw new RuntimeException("Remaining Credits cannot be increased manually");
            }
        }
        Boolean calledFromRazorPayApi = Database.getInstance().getCurrentTransaction().getAttribute("X-FromRazorpayApi");
        if (calledFromRazorPayApi == null){
            calledFromRazorPayApi = false;
        }


        if (model.getRawRecord().isFieldDirty("CAPTURED") && model.isCaptured()){
            if (calledFromRazorPayApi){
                model.getRemainingCredits().increment(model.getPlan().getNumberOfCredits());
            }else{
                throw new RuntimeException("Cannot capture manually like this!");
            }
        }
        if (model.getRawRecord().isFieldDirty("RAZORPAY_PAYMENT_ID") && !model.getReflector().isVoid(model.getRazorpayPaymentId())){
            model.setPurchasedOn(new Timestamp(System.currentTimeMillis()));
        }
        if (model.getId() > 0 && !model.getReflector().isVoid(model.getRazorpayPaymentId()) && !model.isCaptured()){
            TaskManager.instance().executeAsync(new PaymentCapture(model.getId()),false);
        }
    }
}
