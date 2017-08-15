package com.eden.orchid.api.events;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class EventServiceImpl implements EventService {

    private EventEmitter eventEmitter;

    @Inject
    public EventServiceImpl(EventEmitter eventEmitter) {
        this.eventEmitter = eventEmitter;
    }

    @Override
    public EventService getEventService() {
        return this;
    }

    @Override
    public void broadcast(String event, Object... args) {
        eventEmitter.broadcast(event, args);
    }
}
