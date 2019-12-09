package com.eden.orchid.api.registration;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.google.inject.util.Types;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

@Description(value = "The Orchid dependency injection container.", name = "Injection")
public class InjectionServiceImpl implements InjectionService {

    private final Stack<Pair<String, Injector>> injectorStack;

    @Inject
    public InjectionServiceImpl(Injector injector) {
        this.injectorStack = new Stack<>();
        this.injectorStack.push(new Pair<>("root", injector));

    }

    @Override
    public void initialize(OrchidContext context) {
    }

    @Override
    public void pushInjector(String name, List<Pair<String, ?>> childVariables) {
        Module newModule = new OrchidModule() {
            @Override
            protected void configure() {
                Map<Class<?>, List<Pair<String, ?>>> groups = CollectionsKt.groupBy(childVariables, stringPair -> stringPair.getSecond().getClass());

                for(Map.Entry<Class<?>, List<Pair<String, ?>>> entry : groups.entrySet()) {
                    if(entry.getValue().size() > 1) {
                        // if there are multiple instances of the same type, we need to bind them each with their unique names
                        for(Pair<String, ?> child : entry.getValue()) {
                            String itemName = child.getFirst();
                            Object item = child.getSecond();
                            bind((Class<Object>) entry.getKey()).annotatedWith(Names.named(itemName)).toInstance(item);
                        }
                    }
                    else {
                        // if there is a single instance, we do not need to qualify it with the name
                        bind((Class<Object>) entry.getKey()).toInstance(entry.getValue().get(0).getSecond());
                    }
                }
            }
        };

        Injector injector = injectorStack.peek().getSecond().createChildInjector(newModule);

        injectorStack.push(new Pair<>(name, injector));
    }

    @Override
    public void popInjector(String name) {
        if(!injectorStack.peek().getFirst().equals(name)) {
            throw new IllegalArgumentException(
                    Clog.format("Attempt to pop injector failed: given {}, top injector of stack was {}", name, injectorStack.peek().getFirst())
            );
        }

        injectorStack.pop();
    }

    @Override
    public <T> T resolve(Class<T> clazz) {
        try {
            return injectorStack.peek().getSecond().getInstance(clazz);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T> T resolve(Class<T> clazz, String named) {
        try {
            return injectorStack.peek().getSecond().getInstance(Key.get(clazz, Names.named(named)));
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T> T injectMembers(T instance) {
        injectorStack.peek().getSecond().injectMembers(instance);
        return instance;
    }

    public <T> Set<T> resolveSet(Class<T> clazz) {
        try {
            TypeLiteral<Set<T>> lit = (TypeLiteral<Set<T>>) TypeLiteral.get(Types.setOf(clazz));
            Key<Set<T>> key = Key.get(lit);
            Set<T> bindings = injectorStack.peek().getSecond().getInstance(key);

            if (bindings != null) {
                return bindings;
            }
        }
        catch (Exception e) {

        }

        return new TreeSet<>();
    }
}
