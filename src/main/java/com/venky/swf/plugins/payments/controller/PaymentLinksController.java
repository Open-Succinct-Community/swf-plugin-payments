package com.venky.swf.plugins.payments.controller;

import com.venky.swf.controller.ModelController;
import com.venky.swf.controller.annotations.SingleRecordAction;
import com.venky.swf.db.Database;
import com.venky.swf.path.Path;
import com.venky.swf.plugins.background.core.TaskManager;
import com.venky.swf.plugins.payments.db.model.payment.gateway.PaymentLink;
import com.venky.swf.plugins.payments.tasks.PaymentStatusEventDispatcher;
import com.venky.swf.views.View;

public class PaymentLinksController extends ModelController<PaymentLink> {
    public PaymentLinksController(Path path) {
        super(path);
    }
    @SingleRecordAction(icon = "fa-bullhorn")
    public View publish(long id){
        PaymentLink instance = Database.getTable(getModelClass()).get(id);
        TaskManager.instance().executeAsync(new PaymentStatusEventDispatcher(instance.getId()), false);
        return no_content();
    }
}
