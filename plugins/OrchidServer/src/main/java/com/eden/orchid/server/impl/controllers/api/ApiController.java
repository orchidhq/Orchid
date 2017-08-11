package com.eden.orchid.server.impl.controllers.api;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.server.api.OrchidController;
import com.eden.orchid.server.api.methods.Get;
import com.eden.orchid.server.api.OrchidRequest;
import com.eden.orchid.server.api.OrchidResponse;
import org.json.JSONObject;

import javax.inject.Inject;

public class ApiController implements OrchidController {

    private OrchidContext context;

    @Inject
    public ApiController(OrchidContext context) {
        this.context = context;
    }

    @Get(path="/")
    public OrchidResponse doNothing(OrchidRequest request) {
        JSONObject object = new JSONObject();
        object.put("message", "Successful API hit");

        return new OrchidResponse(object);
    }

}
