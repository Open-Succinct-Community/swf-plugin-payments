package com.venky.swf.plugins.payments.controller;

import com.venky.swf.controller.ModelController;
import com.venky.swf.path.Path;
import com.venky.swf.plugins.payments.db.model.payment.Plan;

public class PlansController extends ModelController<Plan> {
    public PlansController(Path path) {
        super(path);
    }


}
