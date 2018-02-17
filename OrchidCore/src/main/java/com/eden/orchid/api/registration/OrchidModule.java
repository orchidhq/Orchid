package com.eden.orchid.api.registration;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.server.admin.AdminList;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Orchid is built on top of the Guice Dependency Injection framework by Google. This framework allows for runtime
 * discovery and injection of dependencies and multibindings, making it ideal for a runtime-plugin-driven framework
 * like Orchid.
 *
 * The OrchidModule adds several convenience methods useful for registering classes within Orchid. It is not necessary
 * to extend OrchidModule, any class that implements the Guice {@link Module} interface and has a public no-arg
 * constructor can be used with Orchid, allowing for maximum interoperability with other libraries. In addition, since
 * Guice is a complete implementation of the JSR-330 interface, even libraries that have been set up for Dependency
 * Injection apart from Guice can be used seamlessly simply by adding the necessary bindings within a Guice Module.
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
public abstract class OrchidModule extends AbstractModule {

    private static final Set<Class<?>> knownSets = new HashSet<>();

    public static <T> AbstractModule of(final Class<T> injectedClass, final T value) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(injectedClass).toInstance(value);
            }
        };
    }

    private void bindKnownSet(final Class<?> setClass) {
        if(!knownSets.contains(setClass)) {
            Multibinder<AdminList> binder = Multibinder.newSetBinder(binder(), AdminList.class);
            binder.addBinding().toInstance(new AdminList() {
                @Override
                public String getKey() {
                    return setClass.getSimpleName();
                }

                @Override
                public Collection getItems() {
                    return Orchid.getInstance().getContext().resolveSet(setClass);
                }
            });

            knownSets.add(setClass);
        }
    }

    protected final <T> void addToSet(Class<T> setClass) {
        Multibinder.newSetBinder(binder(), setClass);
    }

    @SafeVarargs
    protected final <T> void addToSet(Class<T> setClass, Class<? extends T>... objectClasses) {
        if(objectClasses.length > 0) {
            bindKnownSet(setClass);
        }
        Multibinder<T> binder = Multibinder.newSetBinder(binder(), setClass);

        Arrays.stream(objectClasses).forEach(objectClass -> {
            if (setClass.isAssignableFrom(objectClass)) {
                binder.addBinding().to(objectClass);
            }
        });
    }

    @SafeVarargs
    protected final <T> void addToSet(Class<T> setClass, T... objects) {
        if(objects.length > 0) {
            bindKnownSet(setClass);
        }
        Multibinder<T> binder = Multibinder.newSetBinder(binder(), setClass);

        Arrays.stream(objects).forEach(object -> {
            if (setClass.isAssignableFrom(object.getClass())) {
                binder.addBinding().toInstance(object);
            }
        });
    }

}
