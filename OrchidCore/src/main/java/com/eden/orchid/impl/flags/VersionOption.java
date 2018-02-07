package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.Description;

@Description("the version of your library")
public final class VersionOption extends OrchidFlag {

    public VersionOption() {
        super("v", false, null);
    }

}
