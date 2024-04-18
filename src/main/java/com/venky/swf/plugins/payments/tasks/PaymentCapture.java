package com.venky.swf.plugins.payments.tasks;

import com.venky.swf.db.Database;
import com.venky.swf.plugins.background.core.Task;
import com.venky.swf.plugins.payments.db.model.payment.Purchase;

public class PaymentCapture implements Task {
    public PaymentCapture(){

    }
    long invoiceId = -1;
    public PaymentCapture(Purchase invoice) {
        this(invoice.getId());
    }
    public PaymentCapture(long invoiceId){
        this.invoiceId = invoiceId;
    }

    @Override
    public void execute() {
        Database.getInstance().getCurrentTransaction().setAttribute("X-AuthorizedPaymentUpdate",true);

        Purchase first = Database.getTable(Purchase.class).get(invoiceId);
        if (first == null){
            return;
        }
        if (first.isCaptured()){
            return;
        }
        if (first.getReflector().isVoid(first.getPaymentReference())){
            return;
        }
        first.setCaptured(true);
        first.save();

    }
}