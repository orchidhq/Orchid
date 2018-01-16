package com.eden.orchid.impl.themes.templateFunctions;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public final class FindTemplateFunction extends TemplateFunction {

    private final OrchidContext context;

    private final TemplateResolutionStrategy templateResolutionStrategy;

    @Option @StringDefault("page")
    @Getter @Setter
    private String type;

    @Inject
    public FindTemplateFunction(OrchidContext context, TemplateResolutionStrategy templateResolutionStrategy) {
        super("template");
        this.context = context;
        this.templateResolutionStrategy = templateResolutionStrategy;
    }

    @Override
    public String[] parameters() {
        return new String[] {"type"};
    }

    @Override
    public Object apply(Object input) {
        List<String> templates = null;

        if(input != null) {
            if(type.equalsIgnoreCase("layout") && input instanceof OrchidPage) {
                templates = templateResolutionStrategy.getPageLayout((OrchidPage) input);
            }
            else if(type.equalsIgnoreCase("page") && input instanceof OrchidPage) {
                templates = templateResolutionStrategy.getPageTemplate((OrchidPage) input);
            }
            else if(type.equalsIgnoreCase("component") && input instanceof OrchidComponent) {
                templates = templateResolutionStrategy.getComponentTemplate((OrchidComponent) input);
            }
        }

        if (!EdenUtils.isEmpty(templates)) {
            return templates;
        }

        throw new IllegalArgumentException(Clog.format("Could not get templates from type {}", type));
    }

}
