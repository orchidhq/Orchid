package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;

public class AdminThemeOption implements OrchidFlag {

    @Override
    public String getFlag() {
        return "adminTheme";
    }

    @Override
    public String getDescription() {
        return "the fully-qualified classname of your selected Admin Theme";
    }

    @Override
    public Object getDefaultValue() {
        return "Default";
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
