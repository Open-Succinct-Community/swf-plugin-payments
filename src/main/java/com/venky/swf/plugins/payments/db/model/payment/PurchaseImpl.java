package com.venky.swf.plugins.payments.db.model.payment;

import com.venky.core.date.DateUtils;
import com.venky.core.math.DoubleUtils;
import com.venky.swf.db.model.Model;
import com.venky.swf.db.table.ModelImpl;

import java.awt.*;

public class PurchaseImpl extends ModelImpl<Purchase> {
    public PurchaseImpl(Purchase purchase){
        super(purchase);
    }
    public Long getBuyerId(){
        Model b = (Model)getBuyer();
        return b== null ? null : b.getId();
    }
    Buyer buyer = null;
    public Buyer getBuyer(){
        Purchase purchase = getProxy();
        if (buyer == null) {
            if (purchase.getApplicationId() != null) {
                buyer = purchase.getApplication();
            } else if (purchase.getCompanyId() != null) {
                buyer = purchase.getCompany();
            }
        }
        return buyer;
    }
    
    public boolean isExpired(){
        Purchase purchase = getProxy();
        Plan plan = purchase.getPlan();
        if (purchase.getExpiresOn() != null && purchase.getExpiresOn().getTime() < System.currentTimeMillis()){
            //Time based plan
            return true;
        }else if (plan.getNumberOfCredits() > 0) {
            //By Date it expires in future.
            return DoubleUtils.compareTo(purchase.getRemainingCredits().doubleValue(), 0.0D) <= 0;
        }else {
            return false;
            //Days based plan. which is not yet expired.
        }
        
    }
}
