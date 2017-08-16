package com.eden.orchid.api.events;

import com.eden.orchid.api.OrchidContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class EventServiceImpl implements EventService {

    private OrchidContext context;
    private EventEmitter eventEmitter;

    @Inject
    public EventServiceImpl(EventEmitter eventEmitter) {
        this.eventEmitter = eventEmitter;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
    }

    @Override
    public void broadcast(String event, Object... args) {
        eventEmitter.broadcast(event, args);
    }
}
