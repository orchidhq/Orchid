package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import lombok.Data;

import javax.inject.Inject;
import java.util.Set;

@Data
public abstract class OrchidComponent {

    protected String alias;
    protected String defaultAlias;
    protected OrchidContext context;
    protected Set<TemplateResolutionStrategy> strategies;
    protected OrchidResources resources;

    @Inject
    public OrchidComponent(OrchidContext context, Set<TemplateResolutionStrategy> strategies, OrchidResources resources) {
        this.context = context;
        this.strategies = strategies;
        this.resources = resources;
    }

    public void prepare() {

    }

    public abstract String render();

    public String renderTemplate(String... templates) {
        for(TemplateResolutionStrategy templateResolutionStrategy : strategies) {
            for (String template : templateResolutionStrategy.getComponentTemplate(this, templates)) {
                OrchidResource templateResource = resources.getResourceEntry(template);
                if (templateResource != null) {
                    return context.compile(templateResource.getReference().getExtension(), templateResource.getContent(), this);
                }
            }
        }

        return "";
    }

    public String renderString(String extension, String templateString) {
        return context.compile(extension, templateString, this);
    }
}
