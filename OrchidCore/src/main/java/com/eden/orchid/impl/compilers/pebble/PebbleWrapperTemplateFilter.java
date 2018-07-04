package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.google.inject.Provider;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import lombok.Getter;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

@Getter
public final class PebbleWrapperTemplateFilter implements Filter {

    private final Provider<OrchidContext> contextProvider;
    private final String name;
    private final List<String> params;
    private final Class<? extends TemplateFunction> functionClass;

    public PebbleWrapperTemplateFilter(Provider<OrchidContext> contextProvider, String name, List<String> params, Class<? extends TemplateFunction> functionClass) {
        this.contextProvider = contextProvider;
        this.name = name;
        if(params.size() > 0) {
            this.params = params.subList(1, params.size());
        }
        else {
            this.params = params;
        }
        this.functionClass = functionClass;
    }

    @Override
    public List<String> getArgumentNames() {
        return params;
    }

    @Override
    public Object apply(
            Object input,
            Map<String, Object> args,
            PebbleTemplate self,
            EvaluationContext context,
            int lineNumber) throws PebbleException {
        TemplateFunction freshFunction = contextProvider.get().getInjector().getInstance(functionClass);
        freshFunction.extractOptions(contextProvider.get(), args);
        Object output = freshFunction.apply(input);

        if(freshFunction.isSafe()) {
            return new SafeString(output.toString());
        }
        else {
            return output;
        }
    }
}
