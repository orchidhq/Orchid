package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;

public class ThemeOption implements OrchidFlag {

    @Override
    public String getFlag() {
        return "theme";
    }

    @Override
    public String getDescription() {
        return "the fully-qualified classname of your selected Theme object";
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
