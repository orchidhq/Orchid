package com.eden.orchid.api.events;

import lombok.Getter;

@Getter
public abstract class OrchidEvent {

    private Object sender;

    public OrchidEvent(Object sender) {
        this.sender = sender;
    }
}
