package com.eden.orchid.server.server;

import com.eden.orchid.api.resources.OrchidResourceSource;

import javax.inject.Singleton;

@Singleton
public class ServerResourceSource implements OrchidResourceSource {

    private int resourcePriority = 1;

    @Override
    public int getResourcePriority() {
        return resourcePriority;
    }

    @Override
    public void setResourcePriority(int priority) {
        resourcePriority = priority;
    }
}
