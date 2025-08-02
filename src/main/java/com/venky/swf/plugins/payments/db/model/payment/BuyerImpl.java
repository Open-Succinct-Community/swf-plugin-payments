package com.venky.swf.plugins.payments.db.model.payment;

import com.venky.cache.UnboundedCache;
import com.venky.core.date.DateUtils;
import com.venky.swf.db.model.Model;
import com.venky.swf.db.model.reflection.ModelReflector;
import com.venky.swf.db.table.ModelImpl;
import com.venky.swf.routing.Config;
import com.venky.swf.sql.Conjunction;
import com.venky.swf.sql.Expression;
import com.venky.swf.sql.Operator;
import com.venky.swf.sql.Select;

import java.lang.module.ModuleReader;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BuyerImpl<B extends Buyer & Model> extends ModelImpl<B> {
    public BuyerImpl(B proxy){
        super(proxy);
    }
    
    public boolean isProduction(){
        return !Config.instance().isDevelopmentEnvironment();
    }
    
    Map<Boolean,Map<String,Integer>> balanceByEnv = new UnboundedCache<>() {
        @Override
        protected Map<String, Integer> getValue(Boolean key) {
            return null;
        }
    };
    public Map<String,Integer> getBalance(boolean production){
        Map<String,Integer>balance = balanceByEnv.get(production);
        if (balance != null){
            return balance;
        }
        synchronized (this) {
            B buyer = getProxy();

            long today = DateUtils.getStartOfDay(System.currentTimeMillis());
            Purchase purchase = getLatestSubscription(production);
            balance = new HashMap<>() {{
                put("CREDITS", 0);
                put("DAYS", 0);
            }};
            if (purchase != null){
                balance.put("CREDITS",purchase.getRemainingCredits().intValue());
                balance.put("DAYS", DateUtils.compareToDays(purchase.getExpiresOn().getTime(), today));
            }
        }
        return balance;
    }

    public int getTestBalance() {
        return getBalance(false).get("CREDITS");
    }
    public int getProductionBalance() {
        return getBalance(true).get("CREDITS");
    }
    public int getNumDaysLeftInProductionSubscription(){
        return getBalance(true).get("DAYS");
    }
    public int getNumDaysLeftInTestSubscription() {
        return getBalance(false).get("DAYS");
    }

    public Purchase getLatestSubscription(boolean production){
        List<Purchase> purchases = getSubscriptions(production); //Latest subscription active or expired.
        return purchases.isEmpty() ? null : purchases.get(0);
    }
    
    public List<Purchase> getSubscriptions(boolean production){
        B buyer = getProxy();
        Select select = new Select().from(Purchase.class);
        Expression expression = new Expression(select.getPool(), Conjunction.AND);
        List<String> referenceFields = ModelReflector.instance(Purchase.class).getReferenceFields(buyer.getReflector().getModelClass());
        if (referenceFields.size() == 1){
            expression.add(new Expression(select.getPool(),referenceFields.get(0) , Operator.EQ,buyer.getId()));
        }else {
            throw new RuntimeException("Cannot identify buyer!");
        }

        expression.add(new Expression(select.getPool(),"PRODUCTION", Operator.EQ,production));
        expression.add(new Expression(select.getPool(),"CAPTURED", Operator.EQ,true));
        expression.add(new Expression(select.getPool(),"PURCHASED_ON", Operator.NE));
        expression.add(new Expression(select.getPool(),"EFFECTIVE_FROM", Operator.LE , new Date(DateUtils.getStartOfDay(System.currentTimeMillis()))));
        return select.where(expression).orderBy("EFFECTIVE_FROM DESC" , "CREATED_AT DESC"  ).execute(1); //Latest.
    }
    public Purchase getIncompletePurchase(boolean production) {
        B buyer = getProxy();
        Select select = new Select().from(Purchase.class);
        Expression expression = new Expression(select.getPool(), Conjunction.AND);
        List<String> referenceFields = ModelReflector.instance(Purchase.class).getReferenceFields(buyer.getReflector().getModelClass());
        if (referenceFields.size() == 1){
            String referenceField = referenceFields.get(0);
            expression.add(new Expression(select.getPool(),referenceField , Operator.EQ,buyer.getId()));
        }else {
            throw new RuntimeException("Cannot identify buyer!");
        }
        expression.add(new Expression(select.getPool(),"CAPTURED", Operator.EQ,false));
        expression.add(new Expression(select.getPool(),"PRODUCTION", Operator.EQ,production));

        List<Purchase> purchases = select.where(expression).orderBy("ID DESC").execute(1);
        if (purchases.isEmpty()){
            return null;
        }else {
            return purchases.get(0);
        }
    }
}
