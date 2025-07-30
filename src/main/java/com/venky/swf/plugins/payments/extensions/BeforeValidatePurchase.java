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
            Double oldValue = model.getReflector().getJdbcTypeHelper().getTypeRef(double.class).getTypeConverter().valueOf(model.getRawRecord().getOldValue("REMAINING_CREDITS"));
            
            double currValue = model.getRemainingCredits().doubleValue();
            if (oldValue < currValue) {
                throw new RuntimeException("Remaining Credits cannot be increased manually");
            }
        }
        if (model.getRemainingCredits() == null){
            model.setRemainingCredits(new Bucket());
        }
        Boolean authorizedPaymentUpdate = Database.getInstance().getCurrentTransaction().getAttribute("X-AuthorizedPaymentUpdate");
        if (authorizedPaymentUpdate == null){
            Plan plan = model.getPlan();
            authorizedPaymentUpdate = plan.getReflector().isVoid(plan.getSellingPrice());
            if (authorizedPaymentUpdate && model.getRawRecord().isNewRecord()){
                model.setCaptured(true);
                if (plan.isTrialPlan()){
                    model.setSingleUsePlan(true);
                }else {
                    model.setSingleUsePlan(null);//this is important to leave it null. and is nullable as it is part of unique key that allows multiple nulls.
                    //Techniq to avoid counting number of uses.
                }
            }
        }
        
        
        if (model.getStatus() == null){
            model.setStatus(PaymentStatus.created.name());
        }
        


        if (model.getRawRecord().isFieldDirty("CAPTURED") && model.isCaptured()){
            if (!authorizedPaymentUpdate){
                throw new RuntimeException("Cannot capture manually like this!");
            }
            model.setPurchasedOn(new Timestamp(System.currentTimeMillis()));
            
            long today =  DateUtils.getStartOfDay(model.getPurchasedOn().getTime());
            model.setEffectiveFrom(new Date(today));
            
            Plan plan = model.getPlan();
            TypeConverter<Integer> converter = plan.getReflector().getJdbcTypeHelper().getTypeRef(Integer.class).getTypeConverter();
            long tmp = DateUtils.addToMillis(model.getEffectiveFrom().getTime(), Calendar.DAY_OF_YEAR, converter.valueOf(plan.getNumberOfDaysValidity()));
            tmp = DateUtils.addToMillis(tmp, Calendar.MONTH, converter.valueOf(plan.getNumberOfMonthsValidity()));
            tmp = DateUtils.addToMillis(tmp, Calendar.YEAR, converter.valueOf(plan.getNumberOfYearsValidity()));
            if (tmp > model.getEffectiveFrom().getTime()) {
                model.setExpiresOn(new Date(DateUtils.getStartOfDay(tmp)));
            }else {
                model.setExpiresOn(new Date(DateUtils.HIGH_DATE.getTime()));
            }
            model.getRemainingCredits().increment(plan.getNumberOfCredits());
            model.setAuditRemarks("Top Up");
            if (model.getPaymentMadeById() == null){
                model.setPaymentMadeById(model.getCreatorUserId());
            }
            
            Purchase currentSubscription = model.getBuyer().getLatestSubscription(model.isProduction());
            if (currentSubscription != null) {
                model.getRemainingCredits().increment(currentSubscription.getRemainingCredits().doubleValue());
                currentSubscription.getRemainingCredits().decrement(currentSubscription.getRemainingCredits().doubleValue()); //Zero out old sunscription

                if (!currentSubscription.isExpired()){
                    //It is a pure date based plan.
                    model.setExpiresOn(new Date(model.getExpiresOn().getTime()+ (currentSubscription.getExpiresOn().getTime()-today)));
                    currentSubscription.setExpiresOn(new Date(today));
                }

                // Adjust previous overrun.
                model.setAuditRemarks("Top Up with adjustment from last subscription");
                currentSubscription.setAuditRemarks("Close out and carry over Balance to next subscription");
                currentSubscription.save(false);
            }
            
            model.setStatus(PaymentStatus.captured.name());
        }

    }


}
