package com.eden.orchid.api.events;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import lombok.Getter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

@Singleton
public final class EventServiceImpl implements EventService {

    private OrchidContext context;

    private Set<OrchidEventListener> eventListeners;
    @Getter private Set<EventServiceImpl.EventHandler> eventHandlers;
    private Stack<Class<? extends OrchidEvent>> eventsInProgress;

    static class EventHandler {
        public Class<? extends OrchidEvent> eventClass;
        public boolean allowSubclasses;

        public Object acceptor;
        public Method callback;
    }

    @Inject
    public EventServiceImpl(Set<OrchidEventListener> eventListeners) {
        this.eventHandlers = new HashSet<>();
        this.eventsInProgress = new Stack<>();
        this.eventListeners = eventListeners;
    }

    @Override
    public void initialize(OrchidContext context) {
        this.context = context;
        for (OrchidEventListener listener : eventListeners) {
            registerEventListeners(listener);
        }
    }

    @Override
    public void registerEventListeners(OrchidEventListener listener) {
        Arrays.stream(listener.getClass().getDeclaredMethods())
              .filter(method -> method.isAnnotationPresent(On.class))
              .forEach(method -> {
                  On methodAnnotation = method.getAnnotation(On.class);
                  Class<? extends OrchidEvent> eventType = methodAnnotation.value();

                  EventHandler handler = new EventHandler();
                  handler.eventClass = methodAnnotation.value();
                  handler.allowSubclasses = methodAnnotation.subclasses();
                  handler.acceptor = listener;
                  handler.callback = method;

                  boolean addHandler = true;

                  if(handler.callback.getParameterCount() != 1) {
                      addHandler = false;
                      if(handler.allowSubclasses) {
                          Clog.e("Event handler [{}.{}] must accept a single parameter of type [{}] or a subclass of it.", listener.getClass().getSimpleName(), method.getName(), eventType.getSimpleName());
                      }
                      else {
                          Clog.e("Event handler [{}.{}] must accept a single parameter of type [{}]", listener.getClass().getSimpleName(), method.getName(), eventType.getSimpleName());
                      }
                  }

                  if(handler.allowSubclasses) {
                      if(addHandler && !eventType.isAssignableFrom(handler.callback.getParameterTypes()[0])) {
                          addHandler = false;
                          Clog.e("Event handler [{}.{}] must accept a single parameter of type [{}] or a subclass of it.", listener.getClass().getSimpleName(), method.getName(), eventType.getSimpleName());
                      }
                  }
                  else {
                      if(addHandler && !eventType.equals(handler.callback.getParameterTypes()[0])) {
                          addHandler = false;
                          Clog.e("Event handler [{}.{}] must accept a single parameter of type [{}]", listener.getClass().getSimpleName(), method.getName(), eventType.getSimpleName());
                      }
                  }

                  if(addHandler) {
                      this.eventHandlers.add(handler);
                  }
              });
    }

    @Override
    public void deregisterEventListeners(OrchidEventListener listener) {
        eventHandlers.removeIf(eventHandler -> eventHandler.acceptor == listener);
    }

    @Override
    public void broadcast(OrchidEvent event) {
        for (Class<? extends OrchidEvent> inProgress : eventsInProgress) {
            if (event.getClass().equals(inProgress)) {
                throw new IllegalStateException(Clog.format("The event '#{$1}' is already in progress, it cannot be emitted again until this cycle has finished.", event.getClass().toString()));
            }
        }

        eventsInProgress.push(event.getClass());
        Clog.d("Broadcasting event: '#{$1}'", event.getClass().getSimpleName());
        for (EventHandler handler : this.eventHandlers) {
            try {
                callMethod(event, handler);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        eventsInProgress.pop();
    }

    private void callMethod(OrchidEvent event, EventHandler handler) throws Exception {
        Class<?> paramClass = handler.callback.getParameterTypes()[0];

        boolean callMethod = false;

        if(handler.allowSubclasses) {
            if(paramClass.isAssignableFrom(event.getClass())) {
                callMethod = true;
            }
        }
        else {
            if(paramClass.equals(event.getClass())) {
                callMethod = true;
            }
        }

        if(callMethod) {
            handler.callback.invoke(handler.acceptor, event);
        }
    }

}
