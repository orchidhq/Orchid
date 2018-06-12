package com.eden.orchid.api.registration;

import com.eden.orchid.api.OrchidContext;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import javax.inject.Inject;
import java.util.Set;
import java.util.TreeSet;

public class InjectionServiceImpl implements InjectionService {

    private final Injector injector;

    @Inject
    public InjectionServiceImpl(Injector injector) {
        this.injector = injector;
    }

    @Override
    public void initialize(OrchidContext context) {
    }

    @Override
    public Injector getInjector() {
        return injector;
    }

    @Override
    public <T> T resolve(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    public <T> Set<T> resolveSet(Class<T> clazz) {
        Injector injector = getInjector();
        try {
            TypeLiteral<Set<T>> lit = (TypeLiteral<Set<T>>) TypeLiteral.get(Types.setOf(clazz));
            Key<Set<T>> key = Key.get(lit);
            Set<T> bindings = injector.getInstance(key);

            if (bindings != null) {
                return bindings;
            }
        }
        catch (Exception e) {

        }

        return new TreeSet<>();
    }
}
