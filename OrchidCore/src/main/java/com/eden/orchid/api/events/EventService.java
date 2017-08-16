package com.eden.orchid.api.events;

import com.eden.orchid.api.OrchidService;

public interface EventService extends OrchidService {

    default void broadcast(String event, Object... args) {
        getService(EventService.class).broadcast(event, args);
    }

}
