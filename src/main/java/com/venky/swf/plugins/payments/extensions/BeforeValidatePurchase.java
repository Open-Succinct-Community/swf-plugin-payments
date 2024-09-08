package com.venky.swf.plugins.payments.extensions;

import com.venky.core.date.DateUtils;
import com.venky.core.util.Bucket;
import com.venky.swf.db.Database;
import com.venky.swf.db.JdbcTypeHelper.TypeConverter;
import com.venky.swf.db.extensions.BeforeModelValidateExtension;
import com.venky.swf.plugins.payments.db.model.payment.Plan;
import com.venky.swf.plugins.payments.db.model.payment.Purchase;
import com.venky.swf.plugins.payments.db.model.payment.Purchase.PaymentStatus;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

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

        if (model.getRawRecord().isFieldDirty("PAYMENT_REFERENCE") && !model.getReflector().isVoid(model.getPaymentReference())){
            model.setPurchasedOn(new Timestamp(System.currentTimeMillis()));
        }

        if (model.getStatus() == null){
            model.setStatus(PaymentStatus.created.name());
        }

        if (model.getRawRecord().isFieldDirty("CAPTURED") && model.isCaptured()){
            if (authorizedPaymentUpdate){
                Plan plan = model.getPlan();
                model.setStatus(PaymentStatus.captured.name());
                model.getRemainingCredits().increment(plan.getNumberOfCredits());

                TypeConverter<Integer> converter = plan.getReflector().getJdbcTypeHelper().getTypeRef(Integer.class).getTypeConverter();
                if (model.getEffectiveFrom() == null && model.getPurchasedOn() != null){
                    model.setEffectiveFrom(new Date(model.getPurchasedOn().getTime()));
                }
                if (model.getEffectiveFrom() != null) {
                    long tmp = DateUtils.addToMillis(model.getEffectiveFrom().getTime(), Calendar.DAY_OF_YEAR, converter.valueOf(plan.getNumberOfDaysValidity()));
                    tmp = DateUtils.addToMillis(tmp, Calendar.MONTH, converter.valueOf(plan.getNumberOfMonthsValidity()));
                    tmp = DateUtils.addToMillis(tmp, Calendar.YEAR, converter.valueOf(plan.getNumberOfYearsValidity()));
                    if (tmp > model.getEffectiveFrom().getTime()) {
                        model.setExpiresOn(new Date(DateUtils.getStartOfDay(tmp)));
                    }
                }
            }else{
                throw new RuntimeException("Cannot capture manually like this!");
            }
        }

    }


}
