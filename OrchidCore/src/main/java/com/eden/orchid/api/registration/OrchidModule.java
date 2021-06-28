package com.eden.orchid.api.registration;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourcesource.JarResourceSource;
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource;
import com.eden.orchid.api.resources.resourcesource.PluginResourceSource;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.server.annotations.ImportantModularType;
import com.eden.orchid.impl.resources.resourcesource.PluginJarResourceSource;
import com.google.inject.AbstractModule;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.multibindings.Multibinder;

import javax.annotation.Nullable;
import javax.inject.Provider;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import static com.eden.orchid.utilities.OrchidUtils.DEFAULT_PRIORITY;

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
                    return setClass.isAnnotationPresent(ImportantModularType.class);
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

    public final void withResources() {
        withResources(DEFAULT_PRIORITY);
    }
    public final void withResources(int priority) {
        if(hasResources) {
            throw new IllegalStateException("Resources already added to " + this.getClass().getName());
        }
        addToSet(OrchidResourceSource.class, createResourceSource(priority));
        hasResources = true;
        resourcePriority = priority;
    }

    private OrchidResourceSource createResourceSource(int priority) {
        return PluginJarResourceSource.create(this.getClass(), priority, PluginResourceSource.INSTANCE);
    }

    @Nullable
    public Manifest getModuleManifest() {
        OrchidResourceSource resourceSource = createResourceSource(DEFAULT_PRIORITY);
        if(resourceSource instanceof JarResourceSource) {
            return ((JarResourceSource) resourceSource).getManifest();
        }
        else {
            return null;
        }
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
