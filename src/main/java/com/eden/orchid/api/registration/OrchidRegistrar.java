package com.eden.orchid.api.registration;

import com.eden.orchid.api.tasks.OrchidTask;

import java.util.Map;

public interface OrchidRegistrar extends Contextual {
    void registerProvider(OrchidRegistrationProvider object);
    void registerObject(Object object);
    void reorderResourceSources();
    Map<String, OrchidTask> getSiteTasks();

    <T> void addToResolver(T object);
    <T> T resolve(Class<T> clazz);

    <T extends Prioritized> Map<Integer, T> resolveMap(Class<T> clazz);
    <T extends Prioritized> void setMap(Class<T> clazz, Map<Integer, T> map);
}
