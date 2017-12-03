package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public final class AlertFilter implements JtwigFunction {

    public static boolean showLog = false;

    private final OrchidContext context;

    @Inject
    public AlertFilter(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String name() {
        return "alert";
    }

    @Override
    public Collection<String> aliases() {
        return Collections.emptyList();
    }

    @Override
    public Object execute(FunctionRequest request) {
        List<Object> fnParams = request
                .minimumNumberOfArguments(1)
                .maximumNumberOfArguments(2)
                .getArguments();

        if (fnParams.size() == 1) {
            String input = fnParams.get(0).toString();
            return apply(input, "info");
        }
        else if (fnParams.size() == 2) {
            String input = fnParams.get(0).toString();
            String alertLevel = fnParams.get(1).toString();
            return apply(input, alertLevel);
        }
        else {
            return null;
        }
    }

    public Object apply(String input, String alertLevel) {
        OrchidResource resource = context.getResourceEntry("templates/includes/alert.twig");
        if (resource != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("content", input);
            data.put("alertLevel", alertLevel);

            return resource.compileContent(data);
        }
        else {
            return input;
        }
    }
}