package com.eden.orchid.impl.flags;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.Description;

@Description("the task to run")
public final class TaskOption extends OrchidFlag {

    public TaskOption() {
        super("task", true, true, "build");
    }

}
