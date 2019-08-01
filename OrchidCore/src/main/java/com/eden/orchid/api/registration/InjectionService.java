package com.eden.orchid.api.registration;

import com.eden.orchid.api.OrchidService;
import com.google.inject.ImplementedBy;
import kotlin.Pair;

import java.util.List;
import java.util.Set;

@ImplementedBy(InjectionServiceImpl.class)
public interface InjectionService extends OrchidService {


    default void pushInjector(String name, List<Pair<String, ?>> childVariables) {
        getService(InjectionService.class).pushInjector(name, childVariables);
    }

    default void popInjector(String name) {
        getService(InjectionService.class).popInjector(name);
    }

    default <T> T resolve(Class<T> clazz) {
        return getService(InjectionService.class).resolve(clazz);
    }

    default <T> T resolve(Class<T> clazz, String named) {
        return getService(InjectionService.class).resolve(clazz, named);
    }

    default <T> T injectMembers(T instance) {
        return getService(InjectionService.class).injectMembers(instance);
    }

    default <T> Set<T> resolveSet(Class<T> clazz) {
        return getService(InjectionService.class).resolveSet(clazz);
    }

}
