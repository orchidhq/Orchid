package com.eden.orchid.javadoc.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

public class FieldsComponent extends OrchidComponent {

    @Inject
    public FieldsComponent(OrchidContext context, OrchidResources resources, TemplateResolutionStrategy strategy) {
        super(context, resources, strategy);
        this.alias = "javadocClassFields";
    }
}
