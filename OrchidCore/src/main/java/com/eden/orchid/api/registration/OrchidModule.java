package com.eden.orchid.api.registration;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourcesource.PluginResourceSource;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.server.annotations.Extensible;
import com.eden.orchid.impl.resources.PluginJarResourceSource;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.multibindings.Multibinder;

import javax.inject.Provider;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    private boolean hasResources = false;
    private int resourcePriority;

    private static final Set<Class<?>> knownSets = new HashSet<>();

    @Override
    protected void configure() {
        super.configure();
    }

    public static <T> OrchidModule of(final Class<T> injectedClass, final T value) {
        return new OrchidModule() {
            @Override
            protected void configure() {
                bind(injectedClass).toInstance(value);
            }
        };
    }

    private void bindKnownSet(final Class<?> setClass) {
        Provider<OrchidContext> contextProvider = getProvider(OrchidContext.class);
        if(!knownSets.contains(setClass)) {
            Multibinder<AdminList> binder = Multibinder.newSetBinder(binder(), AdminList.class);
            binder.addBinding().toInstance(new AdminList() {

                @Override
                public Class<?> getListClass() {
                    return setClass;
                }

                @Override
                public String getKey() {
                    return setClass.getSimpleName();
                }

                @Override
                public Collection<Class<?>> getItems() {
                    return contextProvider
                            .get()
                            .resolveSet(setClass)
                            .stream()
                            .map(Object::getClass)
                            .collect(Collectors.toList());
                }

                @Override
                public boolean isImportantType() {
                    return setClass.isAnnotationPresent(Extensible.class);
                }
            });

            knownSets.add(setClass);
        }
    }

    public final <T> void addToSet(Class<T> setClass) {
        Multibinder.newSetBinder(binder(), setClass);
    }

    @SafeVarargs
    public final <T> void addToSet(Class<T> setClass, Class<? extends T>... objectClasses) {
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
    public final <T> void addToSet(Class<T> setClass, T... objects) {
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

    public final void withResources(int priority) {
        if(hasResources) {
            throw new IllegalStateException(Clog.format("Resources already added to {}", this.getClass().getName()));
        }
        addToSet(PluginResourceSource.class, new PluginJarResourceSource(this.getClass(), priority));
        hasResources = true;
        resourcePriority = priority;
    }

    public <T> AnnotatedBindingBuilder<T> _bind(Class<T> clazz) {
        return super.bind(clazz);
    }

    public boolean isHasResources() {
        return hasResources;
    }

    public int getResourcePriority() {
        return resourcePriority;
    }
}
