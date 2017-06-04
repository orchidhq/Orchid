package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;

public class VersionOption implements OrchidFlag {

    @Override
    public String getFlag() {
        return "v";
    }

    @Override
    public String getDescription() {
        return "the version of your library";
    }
}
