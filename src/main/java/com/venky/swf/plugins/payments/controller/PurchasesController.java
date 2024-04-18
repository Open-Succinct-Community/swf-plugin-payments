package com.venky.swf.plugins.payments.controller;

import com.venky.swf.controller.ModelController;
import com.venky.swf.controller.annotations.SingleRecordAction;
import com.venky.swf.db.Database;
import com.venky.swf.path.Path;
import com.venky.swf.plugins.background.core.TaskManager;
import com.venky.swf.plugins.payments.db.model.payment.Purchase;
import com.venky.swf.plugins.payments.tasks.PaymentCapture;
import com.venky.swf.views.View;

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


}
