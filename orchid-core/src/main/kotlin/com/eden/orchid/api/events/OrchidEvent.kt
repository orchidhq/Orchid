package com.eden.orchid.api.events

import com.eden.orchid.api.OrchidContext

/**
 * A generic representation of an Event used to communicate intended or completed actions and extend core functionality.
 *
 * @param <T> the type of the sender, used to communicate from where the event was sent.
 *
 * @since v1.0.0
</T> */
abstract class OrchidEvent<T> {

    val sender: T?

    val type: String

    lateinit var context: OrchidContext

    /**
     * Initialize this Event with the Object that is sending it out, typically `this`, but may be a dedicated Sender
     * class object.
     *
     * @param sender the sender
     *
     * @since v1.0.0
     */
    constructor(type: String, sender: T) {
        this.type = type
        this.sender = sender
    }

    /**
     * Initialize this Event with the Object that is sending it out, typically `this`, but may be a dedicated Sender
     * class object. The Event class's simple name is used for the type
     *
     * @param sender the sender
     *
     * @since v1.0.0
     */
    constructor(sender: T?) {
        this.type = this.javaClass.simpleName.toLowerCase().replace("Event".toRegex(), "")
        this.sender = sender
    }

    override fun toString(): String {
        return this.javaClass.simpleName
    }
}
