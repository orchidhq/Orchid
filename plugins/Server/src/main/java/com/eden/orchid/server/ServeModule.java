package com.eden.orchid.server;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.impl.resources.DefaultResourceSource;
import com.eden.orchid.server.server.ServerResourceSource;

public class ServeModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidTask.class, ServeTask.class);
        addToSet(DefaultResourceSource.class, ServerResourceSource.class);
    }
}
