package com.eden.orchid.javadoc.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

public class MethodsComponent extends OrchidComponent {

    @Inject
    public MethodsComponent(OrchidContext context, OrchidResources resources) {
        super(40, "javadocClassMethods", context, resources);
    }
}
