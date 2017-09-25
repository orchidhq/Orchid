package com.eden.orchid.impl.themes;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.Theme;

import javax.inject.Inject;

public class DefaultTheme extends Theme {

    @Inject
    public DefaultTheme(OrchidContext context) {
        super(context, "Default", 100);
    }

}
