package com.venky.swf.plugins.payments.db.model.payment;

import com.venky.core.math.DoubleHolder;
import com.venky.core.util.Bucket;
import com.venky.swf.db.Database;
import com.venky.swf.db.model.Model;
import com.venky.swf.db.table.ModelImpl;
import com.venky.swf.plugins.collab.db.model.participants.admin.Company;
import com.venky.swf.plugins.collab.db.model.user.User;


public class PlanImpl extends ModelImpl<Plan> {
    public PlanImpl(Plan plan){
        super(plan);
    }



    public Double getSellingPrice() {
        int mrp = getProxy().getMaximumRetailPrice();
        double discountPercentage = getProxy().getDiscountPercentage();
        double sellingPrice = mrp * (1 - discountPercentage/100.0);
        return new DoubleHolder(sellingPrice,2).getHeldDouble().doubleValue();
    }
    public void setSellingPrice(Double sellingPrice) {

    }

    public Double getPrice() {
        Plan plan = getProxy();
        return new DoubleHolder(plan.getSellingPrice()/(1.0 + plan.getTaxPercentage()/100.0),2).getHeldDouble().doubleValue();
    }
    public void setPrice(Double price){

    }

    public Double getTax() {
        return getSellingPrice() - getPrice();
    }
    public void setTax(Double tax){

    }
    
    
    public User getUser(){
        com.venky.swf.db.model.User user = Database.getInstance().getCurrentUser();
        if (user != null){
            return user.getRawRecord().getAsProxy(User.class);
        }
        return null;
    }
    
    public <B extends Buyer & Model> Purchase purchase(B forBuyer){
        User user = getUser();
        if (user == null){
            throw  new RuntimeException("Please login to purchase");
        }
        Plan plan = getProxy();
        Purchase purchase = Database.getTable(Purchase.class).newRecord();
        purchase.setPlanId(plan.getId());
        if (Application.class.getSimpleName().equals(forBuyer.getReflector().getModelClass().getSimpleName())){
            purchase.setApplicationId(forBuyer.getId());
        }else {
            purchase.setCompanyId(forBuyer.getId());
        }
        
        purchase.setCaptured(false);
        purchase.setProduction(forBuyer.isProduction());
        purchase.setRemainingCredits(new Bucket());
        purchase.save();
        return purchase;
    }
}

