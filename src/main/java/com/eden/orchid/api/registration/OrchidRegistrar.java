package com.eden.orchid.api.registration;

import java.util.Set;

public interface OrchidRegistrar extends Contextual {
    void registerProvider(OrchidRegistrationProvider object);
    void registerObject(Object object);
    void reorderResourceSources();

    <T> void addToResolver(T object);
    <T> void addToResolver(Class<? super T> clazz, T object);

    <T> T resolve(Class<T> clazz);
    <T> Set<T> resolveSet(Class<T> clazz);
}
