package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.registration.AutoRegister;
import org.json.JSONObject;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AutoRegister
public class TwigCompileAsFilter implements JtwigFunction {

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

        return Orchid.getContext().getTheme().compile(extension, text, new JSONObject(Orchid.getContext().getRoot().toMap()));
    }
}