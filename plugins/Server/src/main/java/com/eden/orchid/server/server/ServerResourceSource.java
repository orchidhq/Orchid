package com.eden.orchid.server.server;

import com.eden.orchid.api.resources.OrchidResourceSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ServerResourceSource extends OrchidResourceSource {

    @Inject
    public ServerResourceSource() {
        setPriority(20);
    }
}
