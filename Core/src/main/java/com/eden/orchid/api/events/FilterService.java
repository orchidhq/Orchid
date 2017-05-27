package com.eden.orchid.api.events;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenPair;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EventListener;
import java.util.Set;

public class FilterService extends BaseBroadcastService<Filter> {

    @Inject
    public FilterService(Set<EventListener> eventListeners) {
        super();

        for (EventListener listener : eventListeners) {
            findListeners(listener, Filter.class);
        }
    }

    public Object filterInternal(String event, Object arg) {
        super.startBroadcast(event);

        Object filteredArg = arg;

        Clog.d("Applying Filter: '#{$1}' (#{$2} args)", new Object[]{event, arg.getClass().getName()});
        for (EdenPair<Method, Object> callback : this.listeners.get(event)) {
            filteredArg = callMethod(callback.first, callback.second, arg);
        }

        super.endBroadcast();

        return filteredArg;
    }

    public <T> Collection<T> filter(String event, Collection<T> arg) {
        return (Collection<T>) filterInternal(event, arg);
    }

    public String filter(String event, String arg) {
        return (String) filterInternal(event, arg);
    }
}
