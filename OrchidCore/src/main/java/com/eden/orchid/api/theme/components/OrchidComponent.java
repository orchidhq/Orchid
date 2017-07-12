package com.eden.orchid.api.theme.components;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
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
    protected OrchidPage page;

    @Inject
    public OrchidComponent(OrchidContext context, OrchidResources resources) {
        this.context = context;
        this.resources = resources;
    }

    public void prepare() {

    }

    public String getContent() {
        return null;
    }

    public String renderString(String extension, String templateString) {
        return context.compile(extension, templateString, this);
    }

    public String getTemplate() {
        if(!EdenUtils.isEmpty(alias)) {
            return Clog.format("[#{$1}.twig, components/#{$1}.twig]", alias);
        }
        else {
            throw new RuntimeException("Component template must be defined in order to render as template");
        }
    }
}
