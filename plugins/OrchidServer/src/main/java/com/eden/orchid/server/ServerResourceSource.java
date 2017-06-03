package com.eden.orchid.server;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ServerResourceSource extends DefaultResourceSource {

    @Inject
    public ServerResourceSource(OrchidContext context) {
        super(context);
        setPriority(20);
    }
}
