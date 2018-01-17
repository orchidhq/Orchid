package com.eden.orchid.api.events;

import lombok.Getter;

/**
 * A generic representation of an Event used to communicate intended or completed actions and extend core functionality.
 *
 * @param <T> the type of the sender, used to communicate from where the event was sent.
 *
 * @since v1.0.0
 */
public abstract class OrchidEvent<T> {

    @Getter
    private final T sender;

    @Getter
    private final String type;

    /**
     * Initialize this Event with the Object that is sending it out, typically `this`, but may be a dedicated Sender
     * class object.
     *
     * @param sender the sender
     *
     * @since v1.0.0
     */
    public OrchidEvent(String type, T sender) {
        this.type = type;
        this.sender = sender;
    }

    /**
     * Initialize this Event with the Object that is sending it out, typically `this`, but may be a dedicated Sender
     * class object. The Event class's simple name is used for the type
     *
     * @param sender the sender
     *
     * @since v1.0.0
     */
    public OrchidEvent(T sender) {
        this.type = this.getClass().getSimpleName().toLowerCase().replaceAll("Event", "");
        this.sender = sender;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
