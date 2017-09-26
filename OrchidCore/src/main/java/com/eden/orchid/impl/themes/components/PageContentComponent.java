package com.eden.orchid.impl.themes.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

public final class PageContentComponent extends OrchidComponent {

    @Inject
    public PageContentComponent(OrchidContext context) {
        super(context, "pageContent", 100);
    }

}
