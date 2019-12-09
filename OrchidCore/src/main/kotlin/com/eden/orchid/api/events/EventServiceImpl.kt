package com.eden.orchid.api.events

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import java.lang.reflect.Method
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description(value = "The Orchid event broadcast system.", name = "Events")
@JvmSuppressWildcards
class EventServiceImpl
@Inject
constructor(
    private val eventListeners: Set<OrchidEventListener>
) : EventService {
    private lateinit var context: OrchidContext
    private val eventHandlers = mutableSetOf<EventHandler>()
    private val eventsInProgress = mutableListOf<Class<out OrchidEvent<*>>>()

    data class EventHandler(
        val eventClass: Class<out OrchidEvent<*>>,
        val allowSubclasses: Boolean = false,
        val acceptor: Any? = null,
        val callback: Method? = null
    )

    override fun initialize(context: OrchidContext) {
        this.context = context
        for (listener in eventListeners) {
            registerEventListeners(listener)
        }
    }

    override fun registerEventListeners(listener: OrchidEventListener) {
        listener.javaClass
            .declaredMethods
            .filter { method -> method.isAnnotationPresent(On::class.java) }
            .forEach { method ->
                val methodAnnotation = method.getAnnotation(On::class.java)!!
                val eventType = methodAnnotation.value.java
                val handler = EventHandler(
                    eventType,
                    methodAnnotation.subclasses,
                    listener,
                    method
                )

                var addHandler = true
                if (handler.callback!!.parameterCount != 1) {
                    addHandler = false
                    if (handler.allowSubclasses) {
                        Clog.e(
                            "Event handler [{}.{}] must accept a single parameter of type [{}] or a subclass of it.",
                            listener.javaClass.simpleName,
                            method.name,
                            eventType.simpleName
                        )
                    } else {
                        Clog.e(
                            "Event handler [{}.{}] must accept a single parameter of type [{}]",
                            listener.javaClass.simpleName,
                            method.name,
                            eventType.simpleName
                        )
                    }
                }
                if (handler.allowSubclasses) {
                    if (addHandler && !eventType.isAssignableFrom(handler.callback.parameterTypes[0])) {
                        addHandler = false
                        Clog.e(
                            "Event handler [{}.{}] must accept a single parameter of type [{}] or a subclass of it.",
                            listener.javaClass.simpleName,
                            method.name,
                            eventType.getSimpleName()
                        )
                    }
                } else {
                    if (addHandler && eventType != handler.callback.parameterTypes[0]) {
                        addHandler = false
                        Clog.e(
                            "Event handler [{}.{}] must accept a single parameter of type [{}]",
                            listener.javaClass.simpleName,
                            method.name,
                            eventType.getSimpleName()
                        )
                    }
                }
                if (addHandler) {
                    this.eventHandlers.add(handler)
                }
            }
    }

    override fun deregisterEventListeners(listener: OrchidEventListener) {
        eventHandlers.removeIf { eventHandler -> eventHandler.acceptor === listener }
    }

    @Synchronized
    override fun broadcast(event: OrchidEvent<*>) {
        for (inProgress in eventsInProgress) {
            if (event.javaClass == inProgress) {
                throw IllegalStateException(
                    Clog.format(
                        "The event \'#{$1}\' is already in progress, it cannot be emitted again until this cycle has finished.",
                        event.javaClass.toString()
                    )
                )
            }
        }
        eventsInProgress.add(event.javaClass)
        event.context = context

        for (handler in this.eventHandlers) {
            try {
                callMethod(event, handler)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }
        eventsInProgress.removeAt(eventsInProgress.size - 1)
    }

    @Throws(Exception::class)
    private fun callMethod(event: OrchidEvent<*>, handler: EventHandler) {
        val paramClass = handler.callback!!.parameterTypes[0]
        var callMethod = false
        if (handler.allowSubclasses) {
            if (paramClass.isAssignableFrom(event.javaClass)) {
                callMethod = true
            }
        } else {
            if (paramClass == event.javaClass) {
                callMethod = true
            }
        }
        if (callMethod) {
            handler.callback.invoke(handler.acceptor, event)
        }
    }

    fun getEventHandlers(): Set<EventHandler> {
        return this.eventHandlers
    }
}
