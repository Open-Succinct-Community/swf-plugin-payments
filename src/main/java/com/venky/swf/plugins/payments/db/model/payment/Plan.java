package com.venky.swf.plugins.payments.db.model.payment;

import com.venky.swf.db.annotations.column.COLUMN_DEF;
import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.IS_VIRTUAL;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.column.defaulting.StandardDefault;
import com.venky.swf.db.annotations.model.MENU;
import com.venky.swf.db.model.Model;
import com.venky.swf.routing.Config;
import com.venky.swf.sql.Conjunction;
import com.venky.swf.sql.Expression;
import com.venky.swf.sql.Operator;
import com.venky.swf.sql.Select;

import java.util.List;


@MENU("Platform Revenue")
public interface Plan extends Model {
    @UNIQUE_KEY
    public String getName();
    public void setName(String name);
    
    @COLUMN_DEF(StandardDefault.BOOLEAN_FALSE)
    @IS_NULLABLE(false)
    boolean isTrialPlan();
    void setTrialPlan(boolean trailPlan);
    
    @COLUMN_DEF(StandardDefault.BOOLEAN_TRUE)
    @IS_NULLABLE(false)
    boolean isAvailable();
    void setAvailable(boolean available);
    

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
    
    
    boolean isTaxIncludedInListPrice();
    void setTaxIncludedInListPrice(boolean taxIncludedInSellingPrice);
    
    @COLUMN_DEF(StandardDefault.BOOLEAN_FALSE)
    boolean isWalletTopUp();
    void setWalletTopUp(boolean walletTopUp);

    @IS_VIRTUAL
    public Double getSellingPrice();

    @IS_VIRTUAL
    public Double getPrice();

    @IS_VIRTUAL
    Double getTax();

    
    List<Purchase> getPurchases();
    
    public Purchase purchase(Buyer forBuyer,boolean production);
    
    public static Plan trialPlan(){
        Select select  = new Select().from(Plan.class);
        select.where(new Expression(select.getPool(), Conjunction.AND).
                add(new Expression(select.getPool(),"AVAILABLE", Operator.EQ,true)).
                add(new Expression(select.getPool(),"TRIAL_PLAN",Operator.EQ,true)));
        List<Plan> plans = select.execute();
        if (plans.size() != 1){
            if (plans.size() > 1){
                Config.instance().getLogger(Plan.class.getName()).warning("Cannot issue any Trail Plan as multiple plans found!!");
            }
            return null;
        }else {
            return plans.get(0);
        }
    }
}
