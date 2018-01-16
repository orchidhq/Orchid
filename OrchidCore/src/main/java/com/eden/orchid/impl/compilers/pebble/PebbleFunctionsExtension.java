package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.google.inject.Provider;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.Function;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public final class PebbleFunctionsExtension extends AbstractPebbleExtension {

    private Set<TemplateFunction> templateFunctions;

    @Inject
    public PebbleFunctionsExtension(Provider<OrchidContext> contextProvider, Set<TemplateFunction> templateFunctions) {
        super(contextProvider);
        this.templateFunctions = templateFunctions;
    }

    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> filterMap = new HashMap<>();

        for (TemplateFunction function : templateFunctions) {
            filterMap.put(function.getName(), new PebbleWrapperTemplateFilter(
                    contextProvider,
                    function.getName(),
                    Arrays.asList(function.parameters()),
                    function.getClass()));
        }

        return filterMap;
    }

    @Override
    public Map<String, Function> getFunctions() {
        Map<String, Function> functionMap = new HashMap<>();

        for (TemplateFunction function : templateFunctions) {
            functionMap.put(function.getName(), new PebbleWrapperTemplateFunction(
                    contextProvider,
                    function.getName(),
                    Arrays.asList(function.parameters()),
                    function.getClass()));
        }

        return functionMap;
    }

}
