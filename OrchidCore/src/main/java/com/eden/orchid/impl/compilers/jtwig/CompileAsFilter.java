package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.api.OrchidContext;
import org.json.JSONObject;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public class CompileAsFilter implements JtwigFunction {

    private OrchidContext context;

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

        List<Object> fnParams = request.maximumNumberOfArguments(2)
                                       .minimumNumberOfArguments(2)
                                       .getArguments();

        String text = fnParams.get(0).toString();
        String extension = fnParams.get(1).toString();

        return context.compile(extension, text, new JSONObject(context.getRoot().toMap()));
    }
}