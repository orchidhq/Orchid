package com.eden.orchid.impl.registration;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.Theme;
import com.eden.orchid.api.registration.AutoRegister;
import com.eden.orchid.api.registration.OrchidRegistrar;
import com.eden.orchid.api.registration.OrchidRegistrationProvider;
import com.eden.orchid.api.resources.OrchidResourceSource;
import com.eden.orchid.utilities.OrchidUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@AutoRegister
public class Registrar implements OrchidRegistrar {

    private Set<OrchidRegistrationProvider> providers = new TreeSet<>();
    private Map<Class<?>, Set> sets = new HashMap<>();
    private Map<Class<?>, Object> allObjects = new HashMap<>();

    @Override
    public void registerProvider(OrchidRegistrationProvider object) {
        providers.add(object);
    }

    private <T> void registerType(Class<?> clazz, T object) {
        if(Comparable.class.isAssignableFrom(clazz)) {
            sets.putIfAbsent(clazz, new TreeSet<T>());
            sets.get(clazz).add(object);
        }
        else {
            sets.putIfAbsent(clazz, new TreeSet<T>(new OrchidUtils.DefaultComparator()));
            sets.get(clazz).add(object);
        }
    }

    @Override
    public <T> void registerObject(T object) {

        // go through all interfaces of this class and its superclasses, adding the object to those sets
        Class<?> type = object.getClass();
        while(!type.equals(Object.class)) {
            registerType(type, object);

            for(Class<?> clazz : type.getInterfaces()) {
                registerType(clazz, object);
            }

            type = type.getSuperclass();
        }

        // hand the object off to RegistrationProviders to deal with as they will
        for (OrchidRegistrationProvider provider : providers) {
            provider.register(object);
        }
    }

    @Override
    public <T> void addToResolver(T object) {
        addToResolver((Class<T>) object.getClass(), object);
    }

    @Override
    public <T> void addToResolver(Class<? super T> clazz, T object) {
        if(!allObjects.containsKey(clazz)) {
            allObjects.put(clazz, object);
        }
    }

    @Override
    public <T> T resolve(Class<T> clazz) {
        T foundObject = null;

        if(allObjects.containsKey(clazz)) {
            foundObject = (T) allObjects.get(clazz);
        }
        else {
            if(clazz.isInterface()) {
                for(Map.Entry<Class<?>, Object> objectEntry : allObjects.entrySet()) {
                    if(clazz.isAssignableFrom(objectEntry.getKey())) {
                        foundObject = (T) objectEntry.getValue();
                        break;
                    }
                }
            }
            else {
                try {
                    foundObject = clazz.newInstance();
                    allObjects.put(clazz, foundObject);
                }
                catch (IllegalAccessException | InstantiationException e) {
                    Clog.e("Class #{$1} could not be created. Make sure it has a public no-arg constructor.", e, new Object[] {clazz.getName()});
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return foundObject;
    }

    @Override
    public <T> Set<T> resolveSet(Class<T> clazz) {
        if(sets.containsKey(clazz)) {
            return sets.get(clazz);
        }

        return new TreeSet<>();
    }

    public void reorderResourceSources() {
        Theme theme = Orchid.getContext().getTheme();

        reorderThemes();

        for(OrchidResourceSource source : resolveSet(OrchidResourceSource.class)) {
            if(source instanceof Theme) {
                if(!source.getClass().isAssignableFrom(theme.getClass())) {
                    source.setResourcePriority(-1);
                }
            }
        }
    }

    private void reorderThemes() {
        Class<?> superclass = Orchid.getContext().getTheme().getClass();
        int i = 0;

        // find the highest priority of any Theme
        int highestThemePriority = 0;
        for(OrchidResourceSource resourceSourceEntry : resolveSet(OrchidResourceSource.class)) {
            if (resourceSourceEntry instanceof Theme) {
                highestThemePriority = Math.max(highestThemePriority, resourceSourceEntry.getResourcePriority());
            }
        }

        // Go through all Themes and set each parent theme as the next-highest Theme priority
        while(!superclass.equals(Theme.class)) {
            for(OrchidResourceSource resourceSourceEntry : resolveSet(OrchidResourceSource.class)) {
                if(resourceSourceEntry instanceof Theme) {
                    Theme theme = (Theme) resourceSourceEntry;
                    if (theme.getClass().equals(superclass)) {
                        theme.setResourcePriority((highestThemePriority) - i);
                        break;
                    }
                }
            }

            i++;
            superclass = superclass.getSuperclass();
        }
    }
}
