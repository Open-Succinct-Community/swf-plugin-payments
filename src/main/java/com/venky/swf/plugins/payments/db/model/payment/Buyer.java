package com.venky.swf.plugins.payments.db.model.payment;

import com.venky.swf.db.annotations.column.IS_VIRTUAL;

import java.util.List;
import java.util.Map;

public interface Buyer {

    @IS_VIRTUAL
    public int getTestBalance();

    @IS_VIRTUAL
    public int getNumDaysLeftInTestSubscription();

    @IS_VIRTUAL
    public int getProductionBalance();

    @IS_VIRTUAL
    public int getNumDaysLeftInProductionSubscription();

    @IS_VIRTUAL
    boolean isProduction();
    
    public List<Purchase> getPurchases();


    @IS_VIRTUAL
    public Purchase getIncompletePurchase(boolean production);
    
    @IS_VIRTUAL
    public Purchase getLatestSubscription(boolean production);

    
    Map<String,Integer> getBalance(boolean production);
}
