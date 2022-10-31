package com.venky.swf.plugins.payments.db.model.payment;

import com.venky.core.math.DoubleHolder;
import com.venky.core.util.Bucket;
import com.venky.swf.db.Database;
import com.venky.swf.db.table.ModelImpl;
import com.venky.swf.plugins.collab.db.model.user.User;
import com.venky.swf.plugins.payments.db.model.Application;


public class PlanImpl extends ModelImpl<Plan> {
    public PlanImpl(Plan plan){
        super(plan);
    }

    public User getUser(){
        com.venky.swf.db.model.User user = Database.getInstance().getCurrentUser();
        if (user != null){
            return user.getRawRecord().getAsProxy(User.class);
        }
        return null;
    }

    public Purchase purchase(Application forApplication){
        User user = getUser();
        if (user == null){
            throw  new RuntimeException("Please login to purchase");
        }
        Plan plan = getProxy();
        Purchase purchase = Database.getTable(Purchase.class).newRecord();
        purchase.setPlanId(plan.getId());
        purchase.setApplicationId(forApplication.getId());
        purchase.setCaptured(false);
        //purchase.setRazorpayPaymentId();
        purchase.setRemainingCredits(new Bucket());
        purchase.save();
        return purchase;
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
}

