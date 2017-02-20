package com.eden.orchid.api.events;

import com.caseyjbrooks.clog.Clog;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

@Singleton
public final class EventEmitter {

    private Map<String, Set<EventListener>> eventListeners;

    private Stack<String> eventsInProgress;

    @Inject
    public EventEmitter(Set<EventListener> eventListeners) {
        this.eventListeners = new HashMap<>();
        this.eventsInProgress = new Stack<>();

        for(EventListener listener : eventListeners) {
            listener.setup(this);
        }
    }

    public void subscribe(String event, EventListener listener) {
        this.eventListeners.putIfAbsent(event, new HashSet<>());

        this.eventListeners.get(event).add(listener);
    }

    public void publish(String event, Object... args) {
        for(String inProgress : eventsInProgress) {
            if(event.equals(inProgress)) {
                throw new IllegalStateException(Clog.format("The event '#{$1}' is already in progress, it cannot be emitted again until this cycle has finished.", new Object[]{event}));
            }
        }

        this.eventListeners.putIfAbsent(event, new HashSet<>());

        eventsInProgress.push(event);
        Clog.d("Starting event: '#{$1}'", new Object[]{event});
        for(EventListener listener : this.eventListeners.get(event)) {
            listener.onEvent(event, args);
        }
        Clog.d("Ending event: '#{$1}'", new Object[]{event});
        eventsInProgress.pop();
    }
}
