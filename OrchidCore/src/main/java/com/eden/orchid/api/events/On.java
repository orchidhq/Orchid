package com.eden.orchid.api.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to denote a Method in an OrchidEventListener class as a callback to a particular Event.
 *
 * @since v1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface On {

    /**
     * The class of the Event to listen for
     *
     * @return event Class
     *
     * @since v1.0.0
     */
    Class<? extends OrchidEvent> value();

    /**
     * If true, subclasses of the value() class with also trigger this method. By default, only an exact Class match
     * will trigger this method
     *
     * @return whether to accept subclasses
     *
     * @since v1.0.0
     */
    boolean subclasses() default false;
}
