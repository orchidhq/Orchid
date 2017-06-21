package com.eden.orchid.materialize;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.Theme;

import javax.inject.Inject;

public class MaterializeTheme extends Theme {

    @Inject
    public MaterializeTheme(OrchidContext context) {
        super(context);
    }
}
