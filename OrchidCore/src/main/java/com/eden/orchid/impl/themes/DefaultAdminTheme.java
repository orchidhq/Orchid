package com.eden.orchid.impl.themes;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.AdminTheme;

import javax.inject.Inject;

public class DefaultAdminTheme extends AdminTheme {

    @Inject
    public DefaultAdminTheme(OrchidContext context) {
        super(context);
    }

    @Override
    public String getKey() {
        return "Default";
    }
}
