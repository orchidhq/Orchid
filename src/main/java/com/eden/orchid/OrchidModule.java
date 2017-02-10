package com.eden.orchid;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public abstract class OrchidModule extends AbstractModule {
    protected <T> void addToSet(Class<T> setClass, Class<? extends T> objectClass) {
        Multibinder<T> binder = Multibinder.newSetBinder(binder(), setClass);
        binder.addBinding().to(objectClass);
    }
}
