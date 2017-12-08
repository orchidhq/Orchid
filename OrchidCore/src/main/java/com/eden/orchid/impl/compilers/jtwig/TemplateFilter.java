package com.eden.orchid.impl.compilers.jtwig;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.mitchellbosecke.pebble.extension.Filter;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.value.Undefined;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Singleton
public final class TemplateFilter implements JtwigFunction, Filter {

    private final TemplateResolutionStrategy templateResolutionStrategy;

    @Inject
    public TemplateFilter(TemplateResolutionStrategy templateResolutionStrategy) {
        this.templateResolutionStrategy = templateResolutionStrategy;
    }

    @Override
    public String name() {
        return "template";
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

        String type = fnParams.get(1).toString();

        return apply(fnParams.get(0), type);
    }

    @Override
    public Object apply(Object input, Map<String, Object> args) {
        return apply(input, args.get("type").toString());
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("type");
    }

    private Object apply(Object input, String type) {
        List<String> templates = null;

        if(input != null && !(input instanceof Undefined)) {
            switch (type) {
                case "layout":
                    templates = templateResolutionStrategy.getPageLayout((OrchidPage) input);
                    break;
                case "page":
                    templates = templateResolutionStrategy.getPageTemplate((OrchidPage) input);
                    break;
                case "component":
                    templates = templateResolutionStrategy.getComponentTemplate((OrchidComponent) input);
                    break;
            }
        }

        if (templates != null) {
            return templates;
        }

        throw new IllegalArgumentException(Clog.format("Could not get templates from type {}", type));
    }

}
