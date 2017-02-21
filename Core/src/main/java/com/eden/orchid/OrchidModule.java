package com.eden.orchid;

import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import javax.inject.Singleton;
import java.util.Arrays;

public abstract class OrchidModule extends AbstractModule {

    @SafeVarargs
    protected final <T> void addToSet(Class<T> setClass, Class<? extends T>... objectClasses) {
        Multibinder<T> binder = Multibinder.newSetBinder(binder(), setClass);

        Arrays.stream(objectClasses).forEach(objectClass -> {
            if(setClass.isAssignableFrom(objectClass)) {
                binder.addBinding().to(objectClass);
            }
        });
    }

    protected final void addTheme(Class<? extends Theme> themeClass) {
        bind(themeClass).in(Singleton.class);

        Multibinder<Theme> themeBinder = Multibinder.newSetBinder(binder(), Theme.class);
        themeBinder.addBinding().to(themeClass).in(Singleton.class);

        Multibinder<DefaultResourceSource> resourceSourceBinder = Multibinder.newSetBinder(binder(), DefaultResourceSource.class);
        resourceSourceBinder.addBinding().to(themeClass).in(Singleton.class);
    }
}
