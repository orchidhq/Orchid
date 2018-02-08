package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.Description;

@Description("the fully-qualified classname of your selected Admin Theme")
public final class AdminThemeOption extends OrchidFlag {

    public AdminThemeOption() {
        super("adminTheme", true, "Default");
    }

}
