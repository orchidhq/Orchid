package com.eden.orchid.javadoc.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

public class SummaryComponent extends OrchidComponent {

    @Inject
    public SummaryComponent(OrchidContext context, OrchidResources resources) {
        super(10, "javadocClassSummary", context, resources);
    }
}
