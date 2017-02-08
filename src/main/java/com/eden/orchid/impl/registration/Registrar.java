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
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.resources.OrchidResourceSource;
import com.eden.orchid.api.tasks.OrchidTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@AutoRegister
public class Registrar implements OrchidRegistrar {

    private Map<Class<?>, Object> allObjects = new HashMap<>();

    public static Map<Integer, OrchidResourceSource> resourceSources   = new TreeMap<>(Collections.reverseOrder());
    public static List<OrchidRegistrationProvider> providers = new ArrayList<>();

    private Map<Integer, OrchidCompiler>         compilers         = new TreeMap<>(Collections.reverseOrder());
    private Map<Integer, OrchidPreCompiler>      precompilers      = new TreeMap<>(Collections.reverseOrder());
    private Map<Integer, OrchidBlockTagHandler>  blockTagHandlers  = new TreeMap<>(Collections.reverseOrder());
    private Map<Integer, OrchidInlineTagHandler> inlineTagHandlers = new TreeMap<>(Collections.reverseOrder());
    private Map<Integer, OrchidGenerator>        generators        = new TreeMap<>(Collections.reverseOrder());
    private Map<Integer, OrchidOption>           options           = new TreeMap<>(Collections.reverseOrder());

    private Map<String, OrchidTask> siteTasks = new HashMap<>();

    private <T extends Prioritized> void addToPriorityMap(Map<Integer, T> map, T object) {
        int priority = object.priority();
        while (map.containsKey(priority)) {
            priority--;
        }
        map.put(priority, object);
    }

    @Override
    public void registerProvider(OrchidRegistrationProvider object) {
        providers.add(object);
    }

    @Override
    public void registerObject(Object object) {
        if (object instanceof OrchidCompiler) {
            addToPriorityMap(compilers, (OrchidCompiler) object);
        }
        if (object instanceof OrchidPreCompiler) {
            addToPriorityMap(precompilers, (OrchidPreCompiler) object);
        }
        if (object instanceof OrchidInlineTagHandler) {
            addToPriorityMap(inlineTagHandlers, (OrchidInlineTagHandler) object);
        }
        if (object instanceof OrchidBlockTagHandler) {
            addToPriorityMap(blockTagHandlers, (OrchidBlockTagHandler) object);
        }
        if (object instanceof OrchidGenerator) {
            addToPriorityMap(generators, (OrchidGenerator) object);
        }
        if (object instanceof OrchidOption) {
            addToPriorityMap(options, (OrchidOption) object);
        }

        if(object instanceof OrchidResourceSource) {
            registerResourceSource((OrchidResourceSource) object);
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
    public <T extends Prioritized> Map<Integer, T> resolveMap(Class<T> clazz) {
        if(clazz.equals(OrchidCompiler.class)) {
            return (Map<Integer, T>) compilers;
        }
        else if(clazz.equals(OrchidPreCompiler.class)) {
            return (Map<Integer, T>) precompilers;
        }
        else if(clazz.equals(OrchidInlineTagHandler.class)) {
            return (Map<Integer, T>) inlineTagHandlers;
        }
        else if(clazz.equals(OrchidBlockTagHandler.class)) {
            return (Map<Integer, T>) blockTagHandlers;
        }
        else if(clazz.equals(OrchidGenerator.class)) {
            return (Map<Integer, T>) generators;
        }
        else if(clazz.equals(OrchidOption.class)) {
            return (Map<Integer, T>) options;
        }

        return new TreeMap<>();
    }

    @Override
    public <T extends Prioritized> void setMap(Class<T> clazz, Map<Integer, T> map) {
        if(clazz.equals(OrchidCompiler.class)) {
            compilers = (Map<Integer, OrchidCompiler>) map;
        }
        else if(clazz.equals(OrchidPreCompiler.class)) {
            precompilers = (Map<Integer, OrchidPreCompiler>) map;
        }
        else if(clazz.equals(OrchidInlineTagHandler.class)) {
            inlineTagHandlers = (Map<Integer, OrchidInlineTagHandler>) map;
        }
        else if(clazz.equals(OrchidBlockTagHandler.class)) {
            blockTagHandlers = (Map<Integer, OrchidBlockTagHandler>) map;
        }
        else if(clazz.equals(OrchidGenerator.class)) {
            generators = (Map<Integer, OrchidGenerator>) map;
        }
        else if(clazz.equals(OrchidOption.class)) {
            options = (Map<Integer, OrchidOption>) map;
        }
    }

    private void registerResourceSource(OrchidResourceSource resourceSource) {
        int priority = resourceSource.getResourcePriority();
        while(resourceSources.containsKey(priority)) {
            priority--;
        }

        resourceSources.put(priority, resourceSource);
    }

    public void reorderResourceSources() {
        Theme theme = Orchid.getContext().getTheme();

        reorderThemes();

        List<OrchidResourceSource> sources = new ArrayList<>();

        for(Map.Entry<Integer, OrchidResourceSource> source : resourceSources.entrySet()) {
            if(source.getValue() instanceof Theme) {
                if(!source.getValue().getClass().isAssignableFrom(theme.getClass())) {
                    source.getValue().setResourcePriority(-1);
                }
            }

            sources.add(source.getValue());
        }

        resourceSources = new TreeMap<>(Collections.reverseOrder());

        for(OrchidResourceSource resourceSource : sources) {
            registerResourceSource(resourceSource);
        }
    }

    private void reorderThemes() {
        Class<?> superclass = Orchid.getContext().getTheme().getClass();
        int i = 0;

        // find the highest priority of any Theme
        int highestThemePriority = 0;
        for(Map.Entry<Integer, OrchidResourceSource> resourceSourceEntry : resourceSources.entrySet()) {
            if (resourceSourceEntry.getValue() instanceof Theme) {
                highestThemePriority = Math.max(highestThemePriority, resourceSourceEntry.getValue().getResourcePriority());
            }
        }

        // Go through all Themes and set each parent theme as the next-highest Theme priority
        while(!superclass.equals(Theme.class)) {
            for(Map.Entry<Integer, OrchidResourceSource> resourceSourceEntry : resourceSources.entrySet()) {
                if(resourceSourceEntry.getValue() instanceof Theme) {
                    Theme theme = (Theme) resourceSourceEntry.getValue();
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
