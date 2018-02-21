package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.Description;

@Description("the base URL to append to generated URLs.")
public final class BaseUrlFlag extends OrchidFlag {

    public BaseUrlFlag() {
        super("baseUrl", true, true, "/");
    }

}
