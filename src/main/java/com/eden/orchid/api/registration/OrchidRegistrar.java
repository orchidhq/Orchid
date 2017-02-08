package com.eden.orchid.api.registration;

import com.eden.orchid.api.tasks.OrchidTask;

import java.util.Map;
import java.util.Set;

public interface OrchidRegistrar extends Contextual {
    void registerProvider(OrchidRegistrationProvider object);
    void registerObject(Object object);
    void reorderResourceSources();
    Map<String, OrchidTask> getSiteTasks();

    <T> void addToResolver(T object);
    <T> void addToResolver(Class<? super T> clazz, T object);
    <T> T resolve(Class<T> clazz);

    <T> Set<T> resolveSet(Class<T> clazz);
}
