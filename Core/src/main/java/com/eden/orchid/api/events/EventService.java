package com.eden.orchid.api.events;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.EventListener;
import java.util.Set;

@Singleton
public final class EventService extends BaseBroadcastService<On> {

    private EventEmitter emitter;

    @Inject
    public EventService(Set<EventListener> eventListeners, EventEmitter emitter) {
        super();
        this.emitter = emitter;
//        for (EventListener listener : eventListeners) {
//            findListeners(listener, On.class);
//        }
    }

    public void broadcast(String event, Object... args) {
//        super.startBroadcast(event);
        emitter.broadcast(event, args);
//        Clog.d("Broadcasting event: '#{$1}' (#{$2} args)", new Object[]{event, (args != null) ? args.length : 0});
//        endBroadcast();
    }
}
