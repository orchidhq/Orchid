package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.Description;

@Description("the development environment. Reads 'config-<environment>.yml' and may alter the behavior of registered components.")
public final class EnvironmentFlag extends OrchidFlag {

    public EnvironmentFlag() {
        super("environment", true, "debug");
    }

}