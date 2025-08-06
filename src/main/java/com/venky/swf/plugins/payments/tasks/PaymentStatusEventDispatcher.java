package com.venky.swf.plugins.payments.tasks;

import com.venky.swf.db.Database;
import com.venky.swf.db.model.application.Event;
import com.venky.swf.plugins.background.core.Task;
import com.venky.swf.plugins.payments.db.model.payment.gateway.PaymentLink;
import com.venky.swf.routing.Config;
import in.succinct.events.PaymentStatusEvent;
import org.json.simple.JSONObject;

public class PaymentStatusEventDispatcher implements Task {
    Long linkId;
    String hostName;
    
    /**
     * Provided for sake or serializability
     */
    @Deprecated
    public PaymentStatusEventDispatcher(){
    
    }
    public PaymentStatusEventDispatcher(Long id){
        this.linkId = id;
        this.hostName = Config.instance().getHostName();
    }
    @Override
    public void execute() {
        Config.instance().setHostName(this.hostName);
        PaymentLink link = Database.getTable(PaymentLink.class).lock(linkId);
        if (!link.isStatusCommunicated()) {
            Event.find("payment_status_update").raise(link.getApplication(), new PaymentStatusEvent() {{
                setTxnReference( link.getTxnReference());
                setStatus(link.getStatus());
                setActive(link.isActive());
                setUri(link.getLinkUri());
                setAmountPaid(link.getAmountPaid());
            }});
            link.setStatusCommunicated(true);
            link.save();
        }
    }
}
