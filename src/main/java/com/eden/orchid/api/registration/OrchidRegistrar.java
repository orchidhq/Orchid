package com.eden.orchid.api.registration;

import java.util.Set;

public interface OrchidRegistrar extends Contextual {

    void reorderResourceSources();

    <T> T resolve(Class<T> clazz);
    <T> Set<T> resolveSet(Class<T> clazz);
}
