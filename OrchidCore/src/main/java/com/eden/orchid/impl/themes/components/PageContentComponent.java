package com.eden.orchid.impl.themes.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

public class PageContentComponent extends OrchidComponent {

    @Inject
    public PageContentComponent(OrchidContext context, OrchidResources resources) {
        super(100, "pageContent", context, resources);
    }
}
