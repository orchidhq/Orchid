package com.eden.orchid;

import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.theme.Theme;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;

import javax.inject.Singleton;
import java.util.Arrays;

public abstract class OrchidModule extends AbstractModule {

    @SafeVarargs
    protected final <T> void addToSet(Class<T> setClass, Class<? extends T>... objectClasses) {
        Multibinder<T> binder = Multibinder.newSetBinder(binder(), setClass);

        Arrays.stream(objectClasses).forEach(objectClass -> {
            if (setClass.isAssignableFrom(objectClass)) {

                if (Prioritized.class.isAssignableFrom(setClass)) {
                    bind(objectClass).in(Singleton.class);
                    binder.addBinding().to(objectClass).in(Singleton.class);
                }
                else {
                    binder.addBinding().to(objectClass);
                }
            }
        });
    }

    protected final void addTheme(Class<? extends Theme> themeClass) {
        bind(themeClass).in(Singleton.class);
        Multibinder.newSetBinder(binder(), Theme.class).addBinding().to(themeClass).in(Singleton.class);
    }

    protected final <T> void addToMap(Class<T> setClass, String key, Class<? extends T> objectClass) {
        MapBinder<String, T> mapbinder = MapBinder.newMapBinder(binder(), String.class, setClass);

        if (setClass.isAssignableFrom(objectClass)) {

            if (Prioritized.class.isAssignableFrom(setClass)) {
                bind(objectClass).in(Singleton.class);
                mapbinder.addBinding(key).to(objectClass).in(Singleton.class);
            }
            else {
                mapbinder.addBinding(key).to(objectClass);
            }
        }

    }
}
