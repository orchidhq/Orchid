package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;

public final class TaskOption implements OrchidFlag {

    @Override
    public String getFlag() {
        return "task";
    }

    @Override
    public String getDescription() {
        return "the task to run";
    }

    @Override
    public Object getDefaultValue() {
        return "build";
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
