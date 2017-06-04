package com.eden.orchid.editorial;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.Theme;

import javax.inject.Inject;

public class EditorialTheme extends Theme {

    @Inject

    public EditorialTheme(OrchidContext context) {
        super(context);
    }
}
