package com.eden.orchid.languages.highlighter;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.escaper.SafeString;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HighlightPrismFilter implements JtwigFunction, Filter {

    private final OrchidContext context;

    @Inject
    public HighlightPrismFilter(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String name() {
        return "highlightPrism";
    }

    @Override
    public Collection<String> aliases() {
        return Collections.emptyList();
    }

    @Override
    public Object execute(FunctionRequest request) {
        List<Object> fnParams = request
                .minimumNumberOfArguments(1)
                .maximumNumberOfArguments(3)
                .getArguments();

        if(fnParams.size() == 1) {
            String input = fnParams.get(0).toString();
            return apply(input, null, null);
        }
        else if(fnParams.size() == 2) {
            String input = fnParams.get(0).toString();
            String language = fnParams.get(1).toString();
            return apply(input, language, null);
        }
        else if(fnParams.size() == 3) {
            String input = fnParams.get(0).toString();
            String language = fnParams.get(1).toString();
            String markedUpLines = fnParams.get(2).toString();
            return apply(input, language, markedUpLines);
        }
        else {
            return null;
        }
    }

    @Override
    public Object apply(Object input, Map<String, Object> args) {
        return new SafeString(apply(
                input.toString(),
                (String) args.getOrDefault("language", "text"),
                (String) args.getOrDefault("lines", null)));
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("language", "lines");
    }

    public String apply(String input, String language, String markedUpLines) {
        if(language != null) {
            language = Clog.format("class=\"language-{} line-numbers\"", language);
        }
        else {
            language = Clog.format("class=\"language-markup line-numbers\"");
        }
        if(markedUpLines != null) {
            markedUpLines = Clog.format("data-line=\"{}\"", markedUpLines);
        }
        else {
            markedUpLines = "";
        }

        return Clog.format("<pre #{$1} #{$2}><code #{$1}>#{$3}</code></pre>", language, markedUpLines, input.trim());
    }
}