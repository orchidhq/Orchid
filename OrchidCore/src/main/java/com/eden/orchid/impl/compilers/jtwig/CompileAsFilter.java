package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.api.OrchidContext;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import org.json.JSONObject;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Singleton
public final class CompileAsFilter implements JtwigFunction, Filter {

    private final OrchidContext context;

    @Inject
    public CompileAsFilter(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String name() {
        return "compileAs";
    }

    @Override
    public Collection<String> aliases() {
        return Collections.emptyList();
    }

    @Override
    public Object execute(FunctionRequest request) {
        List<Object> fnParams = request
                .maximumNumberOfArguments(2)
                .minimumNumberOfArguments(2)
                .getArguments();

        String text = fnParams.get(0).toString();
        String extension = fnParams.get(1).toString();

        return context.compile(extension, text, new JSONObject(context.getOptionsData().toMap()));
    }

    @Override
    public Object apply(Object input, Map<String, Object> args) {
        return new SafeString(context.compile(
                args.getOrDefault("ext", "txt").toString(),
                input.toString(),
                args.getOrDefault("data", null)));
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("ext", "data");
    }
}