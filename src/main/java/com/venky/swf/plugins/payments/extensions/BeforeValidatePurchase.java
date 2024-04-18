package com.venky.swf.plugins.payments.extensions;

import com.venky.core.util.Bucket;
import com.venky.swf.db.Database;
import com.venky.swf.db.extensions.BeforeModelValidateExtension;
import com.venky.swf.plugins.background.core.TaskManager;
import com.venky.swf.plugins.payments.db.model.payment.Purchase;
import com.venky.swf.plugins.payments.db.model.payment.Purchase.PaymentStatus;
import com.venky.swf.plugins.payments.tasks.PaymentCapture;

import java.sql.Timestamp;

public class BeforeValidatePurchase extends BeforeModelValidateExtension<Purchase> {
    static {
        registerExtension(new BeforeValidatePurchase());
    }
    @Override
    public void beforeValidate(Purchase model) {
        if (model.getRawRecord().isFieldDirty("REMAINING_CREDITS")) {
            Double oldValue = (Double) model.getRawRecord().getOldValue("REMAINING_CREDITS");
            double currValue = model.getRemainingCredits().doubleValue();
            if (model.getReflector().getJdbcTypeHelper().getTypeRef(double.class).getTypeConverter().valueOf(oldValue) < currValue) {
                throw new RuntimeException("Remaining Credits cannot be increased manually");
            }
        }
        if (model.getRemainingCredits() == null){
            model.setRemainingCredits(new Bucket());
        }
        Boolean authorizedPaymentUpdate = Database.getInstance().getCurrentTransaction().getAttribute("X-AuthorizedPaymentUpdate");
        if (authorizedPaymentUpdate == null){
            authorizedPaymentUpdate = false;
        }

        if (model.getRawRecord().isFieldDirty("CAPTURED") && model.isCaptured()){
            if (authorizedPaymentUpdate){
                model.setStatus(PaymentStatus.captured.name());
                model.getRemainingCredits().increment(model.getPlan().getNumberOfCredits());
            }else{
                throw new RuntimeException("Cannot capture manually like this!");
            }
        }
        if (model.getRawRecord().isFieldDirty("PAYMENT_REFERENCE") && !model.getReflector().isVoid(model.getPaymentReference())){
            model.setPurchasedOn(new Timestamp(System.currentTimeMillis()));
        }

        if (model.getStatus() == null){
            model.setStatus(PaymentStatus.created.name());
        }

    }

}
