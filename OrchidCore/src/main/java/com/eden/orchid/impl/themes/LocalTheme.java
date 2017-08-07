package com.eden.orchid.impl.themes;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.Theme;

import javax.inject.Inject;

public class LocalTheme extends Theme {

    @Inject
    public LocalTheme(OrchidContext context) {
        super(context);
    }
}
