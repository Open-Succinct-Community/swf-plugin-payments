package com.venky.swf.plugins.payments.db.model.payment;

import com.venky.core.math.DoubleHolder;
import com.venky.swf.db.table.ModelImpl;
import com.venky.swf.plugins.collab.db.model.participants.admin.Company;


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

}

