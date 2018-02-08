package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.Description;

@Description("the key of your selected Theme object")
public final class ThemeOption extends OrchidFlag {

    public ThemeOption() {
        super("theme", true, "DefaultTheme");
    }

}
