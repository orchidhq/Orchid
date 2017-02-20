package com.eden.orchid.server.server;

import com.eden.orchid.impl.resources.DefaultResourceSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ServerResourceSource extends DefaultResourceSource {

    @Inject
    public ServerResourceSource() {
        setPriority(20);
    }
}
