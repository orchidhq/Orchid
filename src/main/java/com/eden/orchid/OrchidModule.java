package com.eden.orchid;

import com.eden.orchid.api.resources.OrchidResourceSource;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import javax.inject.Singleton;

public abstract class OrchidModule extends AbstractModule {

    @SafeVarargs
    protected final <T> void addToSet(Class<T> setClass, Class<? extends T>... objectClasses) {
        Multibinder<T> binder = Multibinder.newSetBinder(binder(), setClass);

        if(objectClasses != null && objectClasses.length > 0) {
            for(Class<? extends T> objectClass : objectClasses) {
                if(setClass.isAssignableFrom(objectClass)) {
                    binder.addBinding().to(objectClass);
                }
            }
        }
    }

    protected final void addTheme(Class<? extends Theme> themeClass) {
        bind(themeClass).in(Singleton.class);

        Multibinder<Theme> themeBinder = Multibinder.newSetBinder(binder(), Theme.class);
        themeBinder.addBinding().to(themeClass);

        Multibinder<OrchidResourceSource> resourceSourceBinder = Multibinder.newSetBinder(binder(), OrchidResourceSource.class);
        resourceSourceBinder.addBinding().to(themeClass);
    }
}
