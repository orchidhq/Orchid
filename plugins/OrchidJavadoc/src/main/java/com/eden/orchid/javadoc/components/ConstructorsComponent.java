package com.eden.orchid.javadoc.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

public class ConstructorsComponent extends OrchidComponent {

    @Inject
    public ConstructorsComponent(OrchidContext context) {
        super(20, "javadocClassCtors", context);
    }
}
