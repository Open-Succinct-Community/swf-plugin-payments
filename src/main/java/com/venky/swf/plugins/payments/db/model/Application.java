package com.venky.swf.plugins.payments.db.model;

import com.venky.swf.db.Database;
import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.IS_VIRTUAL;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.model.HAS_DESCRIPTION_FIELD;
import com.venky.swf.plugins.collab.db.model.CompanySpecific;
import com.venky.swf.plugins.payments.db.model.payment.Purchase;

import java.util.List;

@HAS_DESCRIPTION_FIELD("APP_ID")
public interface Application extends com.venky.swf.db.model.application.Application , CompanySpecific {
    @UNIQUE_KEY("K1,K2")
    @IS_NULLABLE(false)
    public String getAppId();

    static Application find(String appId) {
        Application application = Database.getTable(Application.class).newRecord();
        application.setAppId(appId);
        return Database.getTable(Application.class).find(application,false);
    }

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

