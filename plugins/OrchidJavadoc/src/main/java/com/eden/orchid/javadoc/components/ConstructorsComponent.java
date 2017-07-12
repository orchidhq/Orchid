package com.eden.orchid.javadoc.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

public class ConstructorsComponent extends OrchidComponent {

    @Inject
    public ConstructorsComponent(OrchidContext context, OrchidResources resources, TemplateResolutionStrategy strategy) {
        super(context, resources, strategy);
        this.alias = "javadocClassCtors";
    }
}
