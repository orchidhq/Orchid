package com.eden.orchid.api.events;

import com.eden.orchid.api.OrchidService;

/**
 * The EventService handles cross-application communication and allows disparate parts of the application to communicate
 * intended or performed actions with each other without having to know exactly of each other's existence. OrchidEvents
 * are set up such that any class listening for an event MUST have a strict compile-time dependency on the sender plugin
 * to ensure that listeners know exactly where they are receiving events from. The basic workflow is as follows:
 *
 * # Senders
 *
 * 1) Create a custom class that extends {@link OrchidEvent}. Any custom properties to notify of, or actions that should
 * be taken at that time, should be first-class members of the Event class. Don't make consumers access the members of
 * a field of the Event, communicate intended actions directly within the Event class. This also encourages the scope of
 * events to be rather small and isolated, such that each event does not ask for multiple actions. In that case, send
 * multiple events.
 * 2) Pass the Event to context.broadcast(OrchidEvent event), which internally calls to the EventService implementation.
 * Broadcasts are synchronous, so any actions expected to be performed during this event are finished when the broadcast
 * method returns.
 * 3) Events do not need to be registered in the Module, since it is unlikely to need such information at runtime.
 * Instead, Event information is dissemenated directly through the compile-time dependencies of listeners listening to
 * an explicit Class of events, which can then be inspected by an IDE or viewing the source code of the sender.
 *
 * ### _Notes_
 *
 * It is possible that events may trigger other events. This is allowed, but care must be taken to ensure that only one
 * instance of any given Event class is active at a time. As events are broadcast, they are added to a stack, then
 * popped off when they complete to ensure only one Event class is active at a time. This is to prevent the possibility
 * of infinite recursion when an event calls itself but does not sufficiently define its base case, which may not be so
 * obvious when the same event is called several handlers down the stack.
 *
 * Since a single event may be passed to multiple listeners, with the exact callback order of listeners being undefined,
 * Events should seek only to notify of intended or completed actions, and not expect anything specific to happen within
 * a broadcast. That is, Events should **_not_** be a mechanism to drive core application functionality, but rather
 * should be used to easily communicate application state throughout the app at specific intervals, and to _extend_ the
 * core functionality of the application or plugin, not _implement_ it. A good example of Event functionality would be
 * to notify the entire application that a build is about to start so individual components may clear their local
 * caches. A poor example of event functionality is using events to determine when a build should start; the build
 * should be started with a direct method call, not an Event.
 *
 * # Listeners
 *
 * 1) Create a class that implements {@link OrchidEventListener} and register it in your plugin's Module.
 * 2) Inside that class, create any number of methods annotated with {@link On}. This annotation requires a value of the
 * Event class it is listening for, and a single parameter with a type that matches the class in the annotation. By
 * default, only Events matching that exact class will call back to this method, but you may set `subclasses = true` in
 * the Annotation to indicate that any subclass of that event may also trigger this callback.
 *
 * ### _Notes_
 *
 * The validity of Listeners are checked at initial application creation time. Any Listener whose arguments do not
 * conform to the above specification will issue an error log and be ignored. At runtime, Events should never throw a
 * ClassCastException when trying to trigger a callback, because we already know by that point that a given method can
 * accept a parameter of the Annotation type.
 *
 * Any Exceptions thrown by an EventListener should be wrapped in a generic {@link RuntimeException} and bubbled up to
 * the caller, unchecked. Events are expected to run simply and without Exception, and any potential danger should be
 * handled within the callback, not the caller. In the case that an expection does bubble up to the caller, it may be
 * inspected by getting the root exception with `ExceptionUtils.getRootCause(exception)`.
 *
 * Every Event class has access to its sender object, which uses Generics that may be useful in determining what action
 * to take for a given Event instance.
 *
 * @since v1.0.0
 */
public interface EventService extends OrchidService {

    /**
     * Registers event listeners for an specific object. Classes injected into OrchidEventListener set are automatically
     * registered at injection time, but additional instances may be registered later.
     *
     * @param listener the object containing callback events to register
     *
     * @since v1.0.0
     */
    default void registerEventListeners(OrchidEventListener listener) {
        getService(EventService.class).registerEventListeners(listener);
    }

    /**
     * Removes callbacks from a specific instance from being called.
     *
     * @param listener the object containing callback events to deregister
     *
     * @since v1.0.0
     */
    default void deregisterEventListeners(OrchidEventListener listener) {
        getService(EventService.class).deregisterEventListeners(listener);
    }

    /**
     * Broadcast an intended or completed action to the application to extend the core functionality.
     *
     * @param event the event to broadcast
     *
     * @since v1.0.0
     */
    default void broadcast(OrchidEvent event) {
        getService(EventService.class).broadcast(event);
    }

}
