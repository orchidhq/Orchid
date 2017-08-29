package com.eden.orchid.impl.tasks;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.tasks.OrchidTask;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BuildTask extends OrchidTask {

    private OrchidContext context;

    @Inject
    public BuildTask(OrchidContext context) {
        super(100);
        this.context = context;
    }

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
        context.build();
    }
}
