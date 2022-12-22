package com.venky.swf.plugins.payments.db.model;

import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.IS_VIRTUAL;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.model.HAS_DESCRIPTION_FIELD;
import com.venky.swf.plugins.collab.db.model.CompanyNonSpecific;
import com.venky.swf.plugins.collab.db.model.CompanySpecific;
import com.venky.swf.plugins.payments.db.model.payment.Purchase;

import java.util.List;

@HAS_DESCRIPTION_FIELD("APP_ID")
public interface Application extends com.venky.swf.plugins.collab.db.model.participants.Application {


    public List<Purchase> getPurchases();

    @IS_VIRTUAL
    public int getTestBalance();

    @IS_VIRTUAL
    public int getProductionBalance();


    @IS_VIRTUAL
    public Purchase getActiveSubscription();

    @IS_VIRTUAL
    public Purchase getIncompletePurchase();
}

