package com.eden.orchid.javadoc.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.components.OrchidComponent;

import java.util.Set;

public class MethodsComponent extends OrchidComponent {

    public MethodsComponent(OrchidContext context, Set<TemplateResolutionStrategy> strategies, OrchidResources resources) {
        super(context, strategies, resources);
    }

    @Override
    public String render() {
        return null;
    }
}
