package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.Description;

@Description("the output directory for all generated files")
public final class DestinationDirectoryFlag extends OrchidFlag {

    public DestinationDirectoryFlag() {
        super("d", true, null);
    }

}