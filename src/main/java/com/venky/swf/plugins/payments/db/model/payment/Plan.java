package com.venky.swf.plugins.payments.db.model.payment;

import com.venky.swf.db.annotations.column.COLUMN_DEF;
import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.IS_VIRTUAL;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.column.defaulting.StandardDefault;
import com.venky.swf.db.annotations.model.MENU;
import com.venky.swf.db.model.Model;
import com.venky.swf.plugins.collab.db.model.participants.admin.Company;

import java.util.List;


@MENU("Platform Revenue")
public interface Plan extends Model {
    @UNIQUE_KEY
    public String getName();
    public void setName(String name);

    @COLUMN_DEF(value = StandardDefault.SOME_VALUE,args = "0")
    public int getNumberOfCredits();
    public void setNumberOfCredits(int numberOfCredits);

    @IS_NULLABLE
    public Integer  getNumberOfDaysValidity();
    public void setNumberOfDaysValidity(Integer numberOfDaysValidity);

    @IS_NULLABLE
    public Integer  getNumberOfMonthsValidity();
    public void setNumberOfMonthsValidity(Integer numberOfMonthsValidity);

    @IS_NULLABLE
    public Integer  getNumberOfYearsValidity();
    public void setNumberOfYearsValidity(Integer numberOfYearsValidity);


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


    List<Purchase> getPurchases();

}
