package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;

public final class BaseUrlFlag implements OrchidFlag {

    @Override
    public String getFlag() {
        return "baseUrl";
    }

    @Override
    public String getDescription() {
        return "the base URL to append to generated URLs.";
    }

    @Override
    public Object getDefaultValue() {
        return "/";
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
