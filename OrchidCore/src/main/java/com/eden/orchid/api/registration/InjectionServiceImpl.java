package com.eden.orchid.api.registration;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

import javax.inject.Inject;
import java.util.Set;
import java.util.TreeSet;

@Description(value = "The Orchid dependency injection container.", name = "Injection")
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
    public <T> T resolve(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    @Override
    public <T> T injectMembers(T instance) {
        injector.injectMembers(instance);
        return instance;
    }

    public <T> Set<T> resolveSet(Class<T> clazz) {
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
