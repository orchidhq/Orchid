package com.eden.orchid.api.options.globals;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.TemplateGlobal;
import com.eden.orchid.api.theme.Theme;

public class ThemeGlobal implements TemplateGlobal<Theme> {

    @Override
    public String key() {
        return "theme";
    }

    @Override
    public Theme get(OrchidContext context) {
        return context.getTheme();
    }
}
