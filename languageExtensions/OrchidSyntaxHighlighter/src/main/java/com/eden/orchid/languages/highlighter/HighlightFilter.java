package com.eden.orchid.languages.highlighter;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;
import org.python.util.PythonInterpreter;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HighlightFilter implements JtwigFunction {

    private final OrchidContext context;

    @Inject
    public HighlightFilter(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String name() {
        return "highlight";
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

        if(fnParams.size() == 1) {
            String input = fnParams.get(0).toString();
            return apply(input, null);
        }
        else if(fnParams.size() == 2) {
            String input = fnParams.get(0).toString();
            String language = fnParams.get(1).toString();
            return apply(input, language);
        }
        else {
            return null;
        }
    }

    public Object apply(String input, String language) {
        try {
            PythonInterpreter interpreter = new PythonInterpreter();

            OrchidResource pygmentsScript = context.getResourceEntry("scripts/pygments/pygments.py");

            String pythonScript = pygmentsScript.getContent();

            // Set a variable with the content you want to work with
            interpreter.set("code", input);
            interpreter.set("codeLanguage", ((!EdenUtils.isEmpty(language)) ? language : "java"));

            // Simple use Pygments as you would in Python
            interpreter.exec(pythonScript);

            return interpreter.get("result", String.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return input;
    }
}