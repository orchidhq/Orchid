package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;

public final class DestinationDirectoryFlag implements OrchidFlag {

    @Override
    public String getFlag() {
        return "d";
    }

    @Override
    public String getDescription() {
        return "the output directory for all generated files";
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
