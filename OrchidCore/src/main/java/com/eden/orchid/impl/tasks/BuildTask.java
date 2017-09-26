package com.eden.orchid.impl.tasks;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.tasks.OrchidTask;
import com.google.inject.Provider;

import javax.inject.Inject;

public final class BuildTask extends OrchidTask {

    private final Provider<OrchidContext> contextProvider;

    @Inject
    public BuildTask(Provider<OrchidContext> contextProvider) {
        super(100);
        this.contextProvider = contextProvider;
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
        contextProvider.get().build();
    }
}
