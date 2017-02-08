package com.eden.orchid.impl.registration;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.Theme;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.compilers.OrchidPreCompiler;
import com.eden.orchid.api.docParser.OrchidBlockTagHandler;
import com.eden.orchid.api.docParser.OrchidInlineTagHandler;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.OrchidOption;
import com.eden.orchid.api.registration.AutoRegister;
import com.eden.orchid.api.registration.OrchidRegistrar;
import com.eden.orchid.api.registration.OrchidRegistrationProvider;
import com.eden.orchid.api.resources.OrchidResourceSource;
import com.eden.orchid.api.tasks.OrchidTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@AutoRegister
public class Registrar implements OrchidRegistrar {

    private Map<Class<?>, Object> allObjects = new HashMap<>();

    public static List<OrchidRegistrationProvider> providers = new ArrayList<>();

    private Set<OrchidResourceSource>   resourceSources   = new TreeSet<>();
    private Set<OrchidCompiler>         compilers         = new TreeSet<>();
    private Set<OrchidPreCompiler>      precompilers      = new TreeSet<>();
    private Set<OrchidBlockTagHandler>  blockTagHandlers  = new TreeSet<>();
    private Set<OrchidInlineTagHandler> inlineTagHandlers = new TreeSet<>();
    private Set<OrchidGenerator>        generators        = new TreeSet<>();
    private Set<OrchidOption>           options           = new TreeSet<>();

    private Map<String, OrchidTask> siteTasks = new HashMap<>();

    @Override
    public void registerProvider(OrchidRegistrationProvider object) {
        providers.add(object);
    }

    @Override
    public void registerObject(Object object) {
        if (object instanceof OrchidCompiler) {
            compilers.add((OrchidCompiler) object);
        }
        if (object instanceof OrchidPreCompiler) {
            precompilers.add((OrchidPreCompiler) object);
        }
        if (object instanceof OrchidInlineTagHandler) {
            inlineTagHandlers.add((OrchidInlineTagHandler) object);
        }
        if (object instanceof OrchidBlockTagHandler) {
            blockTagHandlers.add((OrchidBlockTagHandler) object);
        }
        if (object instanceof OrchidGenerator) {
            generators.add((OrchidGenerator) object);
        }
        if (object instanceof OrchidOption) {
            options.add((OrchidOption) object);
        }

        if(object instanceof OrchidResourceSource) {
            resourceSources.add((OrchidResourceSource) object);
        }
        if (object instanceof OrchidTask) {
            OrchidTask task = (OrchidTask) object;
            siteTasks.put(task.getName(), task);
        }

        for (OrchidRegistrationProvider provider : providers) {
            provider.register(object);
        }
    }

    @Override
    public <T> void addToResolver(T object) {
        if(!allObjects.containsKey(object.getClass())) {
            allObjects.put(object.getClass(), object);
        }
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
        if(clazz.equals(OrchidResourceSource.class)) {
            return (Set<T>) resourceSources;
        }
        else if(clazz.equals(OrchidCompiler.class)) {
            return (Set<T>) compilers;
        }
        else if(clazz.equals(OrchidPreCompiler.class)) {
            return (Set<T>) precompilers;
        }
        else if(clazz.equals(OrchidInlineTagHandler.class)) {
            return (Set<T>) inlineTagHandlers;
        }
        else if(clazz.equals(OrchidBlockTagHandler.class)) {
            return (Set<T>) blockTagHandlers;
        }
        else if(clazz.equals(OrchidGenerator.class)) {
            return (Set<T>) generators;
        }
        else if(clazz.equals(OrchidOption.class)) {
            return (Set<T>) options;
        }

        return new TreeSet<>();
    }

    public void reorderResourceSources() {
        Theme theme = Orchid.getContext().getTheme();

        reorderThemes();

        for(OrchidResourceSource source : resourceSources) {
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
        for(OrchidResourceSource resourceSourceEntry : resourceSources) {
            if (resourceSourceEntry instanceof Theme) {
                highestThemePriority = Math.max(highestThemePriority, resourceSourceEntry.getResourcePriority());
            }
        }

        // Go through all Themes and set each parent theme as the next-highest Theme priority
        while(!superclass.equals(Theme.class)) {
            for(OrchidResourceSource resourceSourceEntry : resourceSources) {
                if(resourceSourceEntry instanceof Theme) {
                    Clog.v("Reordering theme #{$1}", new Object[]{resourceSourceEntry.getClass().getName()});
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

    @Override
    public Map<String, OrchidTask> getSiteTasks() {
        return siteTasks;
    }
}
