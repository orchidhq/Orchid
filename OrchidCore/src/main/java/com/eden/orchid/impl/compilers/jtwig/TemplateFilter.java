package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public class TemplateFilter implements JtwigFunction {

    private TemplateResolutionStrategy templateResolutionStrategy;

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

        switch (type) {
            case "layout":
                templates = templateResolutionStrategy.getPageLayout((OrchidPage) fnParams.get(0));
                break;
            case "page":
                templates = templateResolutionStrategy.getPageLayout((OrchidPage) fnParams.get(0));
                break;
            case "component":
                templates = templateResolutionStrategy.getComponentTemplate((OrchidComponent) fnParams.get(0));
                break;
        }

        if (templates != null) {
            return "[" + String.join(",", templates) + "]";
        }

        throw new IllegalArgumentException("Could not get templates");
    }
}
