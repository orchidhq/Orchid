package com.eden.orchid.api.registration;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import java.util.Arrays;

public abstract class OrchidModule extends AbstractModule {

    public static <T> AbstractModule of(final Class<T> injectedClass, final T value) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(injectedClass).toInstance(value);
            }
        };
    }

    @SafeVarargs
    protected final <T> void addToSet(Class<T> setClass, Class<? extends T>... objectClasses) {
        Multibinder<T> binder = Multibinder.newSetBinder(binder(), setClass);

        Arrays.stream(objectClasses).forEach(objectClass -> {
            if (setClass.isAssignableFrom(objectClass)) {
                binder.addBinding().to(objectClass);
            }
        });
    }

    protected final <T> void addToSet(Class<T> setClass, T... objects) {
        Multibinder<T> binder = Multibinder.newSetBinder(binder(), setClass);

        Arrays.stream(objects).forEach(object -> {
            if (setClass.isAssignableFrom(object.getClass())) {
                binder.addBinding().toInstance(object);
            }
        });
    }
}
