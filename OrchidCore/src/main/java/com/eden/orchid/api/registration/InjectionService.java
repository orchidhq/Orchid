package com.eden.orchid.api.registration;

import com.eden.orchid.api.OrchidService;
import com.google.inject.ImplementedBy;
import com.google.inject.Injector;

import java.util.Set;

@ImplementedBy(InjectionServiceImpl.class)
public interface InjectionService extends OrchidService {

    default Injector getInjector() {
        return getService(InjectionService.class).getInjector();
    }

    default <T> T resolve(Class<T> clazz) {
        return getService(InjectionService.class).resolve(clazz);
    }

    default <T> Set<T> resolveSet(Class<T> clazz) {
        return getService(InjectionService.class).resolveSet(clazz);
    }

}
