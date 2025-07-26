package com.venky.swf.plugins.payments.db.model.payment;

import com.venky.swf.plugins.payments.db.model.payment.gateway.PaymentGateway;

import java.util.List;

public interface Company extends com.venky.swf.plugins.collab.db.model.participants.admin.Company , Buyer{
    List<PaymentGateway> getPaymentGateways();
}
