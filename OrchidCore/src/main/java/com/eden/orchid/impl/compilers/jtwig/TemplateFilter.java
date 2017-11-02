package com.eden.orchid.impl.compilers.jtwig;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;
import org.jtwig.value.Undefined;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public final class TemplateFilter implements JtwigFunction {

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

        List<String> templates = null;

        if(fnParams.get(0) != null && !(fnParams.get(0) instanceof Undefined)) {
            switch (type) {
                case "layout":
                    templates = templateResolutionStrategy.getPageLayout((OrchidPage) fnParams.get(0));
                    break;
                case "page":
                    templates = templateResolutionStrategy.getPageTemplate((OrchidPage) fnParams.get(0));
                    break;
                case "component":
                    templates = templateResolutionStrategy.getComponentTemplate((OrchidComponent) fnParams.get(0));
                    break;
            }
        }

        if (templates != null) {
            return templates;
        }

        throw new IllegalArgumentException(Clog.format("Could not get templates from type {}", type));
    }
}
