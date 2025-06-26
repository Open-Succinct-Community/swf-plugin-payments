package com.venky.swf.plugins.payments.tasks;

import com.venky.swf.db.Database;
import com.venky.swf.db.model.application.Event;
import com.venky.swf.plugins.background.core.Task;
import com.venky.swf.plugins.payments.db.model.payment.gateway.PaymentLink;
import in.succinct.events.PaymentStatusEvent;
import org.json.simple.JSONObject;

public class PaymentStatusEventDispatcher implements Task {
    Long linkId;
    public PaymentStatusEventDispatcher(Long id){
        this.linkId = id;
    }
    @Override
    public void execute() {
        PaymentLink link = Database.getTable(PaymentLink.class).get(linkId);
        if (!link.isStatusCommunicated()) {
            Event.find("payment_status_update").raise(link.getApplication(), new PaymentStatusEvent() {{
                setTxnReference( link.getTxnReference());
                setStatus(link.getStatus());
                setActive(link.isActive());
                setUri(link.getLinkUri());
                setAmountPaid(link.getAmountPaid());
            }});
            link.setStatusCommunicated(true);
        }
    }
}
