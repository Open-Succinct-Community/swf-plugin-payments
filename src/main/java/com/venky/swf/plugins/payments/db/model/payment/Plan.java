package com.venky.swf.plugins.payments.db.model.payment;

import com.venky.swf.db.annotations.column.COLUMN_DEF;
import com.venky.swf.db.annotations.column.IS_VIRTUAL;
import com.venky.swf.db.annotations.column.defaulting.StandardDefault;
import com.venky.swf.db.model.Model;
import com.venky.swf.plugins.payments.db.model.Application;


public interface Plan extends Model {
    public String getName();
    public void setName(String name);

    @COLUMN_DEF(value = StandardDefault.SOME_VALUE,args = "0")
    public int getNumberOfCredits();
    public void setNumberOfCredits(int numberOfCredits);

    public int getMaximumRetailPrice();
    public void setMaximumRetailPrice(int maximumRetailPrice);

    @COLUMN_DEF(value = StandardDefault.SOME_VALUE,args = "18.0")
    public double getTaxPercentage();
    public void setTaxPercentage(double taxPercentage);



    @COLUMN_DEF(value = StandardDefault.SOME_VALUE,args = "0.0")
    public double getDiscountPercentage();
    public void setDiscountPercentage( double discountPercentage);

    @IS_VIRTUAL
    public Double getSellingPrice();

    @IS_VIRTUAL
    public Double getPrice();

    @IS_VIRTUAL
    Double getTax();

    public Purchase purchase(Application forApplication);

}
