package com.eden.orchid.api.theme.components;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Data;

import javax.inject.Inject;

@Data
public abstract class OrchidComponent {

    protected String alias;
    protected String defaultAlias;
    protected OrchidContext context;
    protected OrchidResources resources;
    protected TemplateResolutionStrategy templateResolutionStrategy;
    protected OrchidPage page;

    @Inject
    public OrchidComponent(OrchidContext context, OrchidResources resources, TemplateResolutionStrategy templateResolutionStrategy) {
        this.context = context;
        this.resources = resources;
        this.templateResolutionStrategy = templateResolutionStrategy;
    }

    public void prepare(OrchidPage page) {
        this.page = page;
    }

    public String getContent() {
        return null;
    }

    public String renderString(String extension, String templateString) {
        return context.compile(extension, templateString, this);
    }

    public String getTemplate() {
        if(!EdenUtils.isEmpty(alias)) {
            return Clog.format("[#{$1 | join(',')}]", templateResolutionStrategy.getComponentTemplate(this));
        }
        else {
            throw new RuntimeException("Component alias must be defined in order to render as template");
        }
    }
}
