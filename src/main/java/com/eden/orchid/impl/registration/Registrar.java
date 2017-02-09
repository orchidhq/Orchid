package com.eden.orchid.impl.registration;

import com.eden.orchid.Orchid;
import com.eden.orchid.Theme;
import com.eden.orchid.api.registration.OrchidRegistrar;
import com.eden.orchid.api.resources.OrchidResourceSource;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import javax.inject.Singleton;
import java.util.Set;
import java.util.TreeSet;

@Singleton
public class Registrar implements OrchidRegistrar {

    @Override
    public <T> T resolve(Class<T> clazz) {
        return getInjector().getInstance(clazz);
    }

    @Override
    public <T> Set<T> resolveSet(Class<T> clazz) {
        try {
            TypeLiteral<Set<T>> lit = (TypeLiteral<Set<T>>) TypeLiteral.get(Types.setOf(clazz));
            Key<Set<T>> key = Key.get(lit);
            Set<T> bindings = getInjector().getInstance(key);

            if(bindings != null) {
                return bindings;
            }
        }
        catch (Exception e) {

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
