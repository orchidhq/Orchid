package com.eden.orchid.server;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.server.api.OrchidController;
import com.eden.orchid.server.impl.controllers.admin.AdminController;
import com.eden.orchid.server.impl.controllers.api.ApiController;

import java.util.EventListener;

public class ServerModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidTask.class, ServeTask.class);
        addToSet(DefaultResourceSource.class, ServerResourceSource.class);

        addToSet(EventListener.class, ServeTask.class);
        addToSet(OrchidController.class,
                AdminController.class,
                ApiController.class);
    }
}
