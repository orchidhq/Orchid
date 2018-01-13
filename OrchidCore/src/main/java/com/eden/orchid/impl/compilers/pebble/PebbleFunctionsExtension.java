package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.google.inject.Provider;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.extension.NodeVisitorFactory;
import com.mitchellbosecke.pebble.extension.Test;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import com.mitchellbosecke.pebble.operator.BinaryOperator;
import com.mitchellbosecke.pebble.operator.UnaryOperator;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public final class PebbleFunctionsExtension implements Extension {

    private Provider<OrchidContext> contextProvider;
    private Set<TemplateFunction> filters;

    @Inject
    public PebbleFunctionsExtension(Provider<OrchidContext> contextProvider, Set<TemplateFunction> filters) {
        this.contextProvider = contextProvider;
        this.filters = filters;
    }

    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> filterMap = new HashMap<>();

        for (TemplateFunction function : filters) {
            Filter filter = new Filter() {
                @Override
                public Object apply(Object input, Map<String, Object> args) {
                    TemplateFunction freshFunction = contextProvider.get().getInjector().getInstance(function.getClass());
                    JSONObject object = new JSONObject(args);
                    freshFunction.extractOptions(contextProvider.get(), object);
                    Object output = freshFunction.apply(input);

                    if(freshFunction.isSafe()) {
                        return new SafeString(output.toString());
                    }
                    else {
                        return output;
                    }
                }

                @Override
                public List<String> getArgumentNames() {
                    return Arrays.asList(function.parameters());
                }
            };

            filterMap.put(function.getName(), filter);
        }

        return filterMap;
    }

    @Override
    public Map<String, Test> getTests() {
        return null;
    }

    @Override
    public Map<String, Function> getFunctions() {
        Map<String, Function> filterMap = new HashMap<>();

        for (TemplateFunction function : filters) {
            Function func = new Function() {
                @Override
                public Object execute(Map<String, Object> args) {
                    TemplateFunction freshFunction = contextProvider.get().getInjector().getInstance(function.getClass());
                    JSONObject object = new JSONObject(args);
                    freshFunction.extractOptions(contextProvider.get(), object);

                    Object output = freshFunction.apply(null);

                    if(freshFunction.isSafe()) {
                        return new SafeString(output.toString());
                    }
                    else {
                        return output;
                    }
                }

                @Override
                public List<String> getArgumentNames() {
                    return Arrays.asList(function.parameters());
                }
            };

            filterMap.put(function.getName(), func);
        }

        return filterMap;
    }

    @Override
    public List<TokenParser> getTokenParsers() {
        return null;
    }

    @Override
    public List<BinaryOperator> getBinaryOperators() {
        return null;
    }

    @Override
    public List<UnaryOperator> getUnaryOperators() {
        return null;
    }

    @Override
    public Map<String, Object> getGlobalVariables() {
        return null;
    }

    @Override
    public List<NodeVisitorFactory> getNodeVisitors() {
        return null;
    }

}
