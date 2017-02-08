package com.eden.orchid.impl.tasks;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.registration.AutoRegister;

@AutoRegister
public class BuildTask implements OrchidTask {
    @Override
    public String getName() {
        return "build";
    }

    @Override
    public String getDescription() {
        return "Run the main Orchid build process. This is the default OrchidTask for Javadoc and if no other valid task is specified.";
    }

    @Override
    public void run() {
        Orchid.getContext().build();
    }
}
