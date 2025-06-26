package com.venky.swf.plugins.payments.extensions;

import com.venky.swf.plugins.collab.extensions.participation.CompanySpecificParticipantExtension;
import com.venky.swf.plugins.payments.db.model.payment.gateway.PaymentGateway;

public class PaymentGatewayParticipantExtension extends CompanySpecificParticipantExtension<PaymentGateway> {
    static {
        registerExtension(new PaymentGatewayParticipantExtension());
    }
}
