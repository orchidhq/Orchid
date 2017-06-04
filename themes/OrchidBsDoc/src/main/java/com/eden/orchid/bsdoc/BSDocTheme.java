package com.eden.orchid.bsdoc;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.Theme;

import javax.inject.Inject;

public class BSDocTheme extends Theme {

    @Inject

    public BSDocTheme(OrchidContext context) {
        super(context);
    }
}
