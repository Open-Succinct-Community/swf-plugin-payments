package com.venky.swf.plugins.payments.controller;

import com.venky.core.util.Bucket;
import com.venky.swf.controller.ModelController;
import com.venky.swf.db.Database;
import com.venky.swf.path.Path;
import com.venky.swf.plugins.collab.db.model.participants.admin.Company;
import com.venky.swf.plugins.payments.db.model.payment.Plan;
import com.venky.swf.plugins.payments.db.model.payment.Purchase;
import com.venky.swf.pm.DataSecurityFilter;
import com.venky.swf.views.ForwardedView;
import com.venky.swf.views.View;

import java.sql.Timestamp;
import java.util.List;

public class PlansController extends ModelController<Plan> {
    public PlansController(Path path) {
        super(path);
    }


}
