package com.venky.swf.plugins.payments.controller;

import com.venky.swf.controller.ModelController;
import com.venky.swf.controller.annotations.SingleRecordAction;
import com.venky.swf.db.Database;
import com.venky.swf.db.model.Model;
import com.venky.swf.path.Path;
import com.venky.swf.plugins.background.core.TaskManager;
import com.venky.swf.plugins.collab.db.model.participants.Application;
import com.venky.swf.plugins.payments.db.model.payment.Company;
import com.venky.swf.plugins.payments.db.model.payment.Plan;
import com.venky.swf.plugins.payments.db.model.payment.Purchase;
import com.venky.swf.plugins.payments.tasks.PaymentCapture;
import com.venky.swf.views.View;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PurchasesController extends ModelController<Purchase> {
    public PurchasesController(Path path) {
        super(path);
    }

    @Override
    protected View show(Purchase record) {
        return super.show(record);
    }
    @Override
    protected View blank(Purchase record) {
        return super.blank(record);
    }

    @SingleRecordAction(icon = "glyphicon-check", tooltip = "Capture the payment")
    public View capture(long invoiceId){
        Purchase invoice = Database.getTable(Purchase.class).get(invoiceId);

        TaskManager.instance().executeAsync(new PaymentCapture(invoice),false);
        if (getIntegrationAdaptor() == null){
            if (!invoice.isCaptured() && !invoice.getReflector().isVoid(invoice.getPaymentReference())){
                getPath().addInfoMessage("Capturing Payment! Transaction Reference : " + invoice.getPaymentReference());
            }else {
                getPath().addInfoMessage("Nothing to capture");
            }

            return  back();
        }else {
            return getIntegrationAdaptor().createStatusResponse(getPath(),null);
        }
    }
    
    @Override
    protected Map<Class<? extends Model>, List<String>> getIncludedModelFields() {
        Map<Class<? extends Model>, List<String>> map = super.getIncludedModelFields();
        if (!map.containsKey(Application.class)){
            map.put(Application.class, Arrays.asList("ID","APP_ID","CHANGE_SECRET", "TEST_BALANCE", "PRODUCTION_BALANCE"));
        }
        if (!map.containsKey(Company.class)){
            map.put(Company.class, Arrays.asList("ID", "NAME","TEST_BALANCE", "PRODUCTION_BALANCE"));
        }
        if (!map.containsKey(Plan.class)){
            map.put(Plan.class, Arrays.asList("ID","NAME","MAXIMUM_RETAIL_PRICE", "NUMBER_OF_CREDITS","TAX_PERCENTAGE"));
        }
        return map;
    }

}
