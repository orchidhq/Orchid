package com.eden.orchid.api.events;

public interface EventListener {

    void setup(EventEmitter emitter);

    void onEvent(String event, Object... args);
}
