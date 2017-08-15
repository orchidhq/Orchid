package com.eden.orchid.api.events;

public interface EventService {

    EventService getEventService();

    default void broadcast(String event, Object... args) {
        getEventService().broadcast(event, args);
    }

}
