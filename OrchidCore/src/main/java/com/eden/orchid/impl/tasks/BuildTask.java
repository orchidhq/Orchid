package com.eden.orchid.impl.tasks;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.TaskService;
import com.google.inject.Provider;

import javax.inject.Inject;

@Description("Run the main Orchid build process, then exit. This is the default task."
)
public final class BuildTask extends OrchidTask {

    private final Provider<OrchidContext> contextProvider;

    @Inject
    public BuildTask(Provider<OrchidContext> contextProvider) {
        super(100, "build", TaskService.TaskType.BUILD);
        this.contextProvider = contextProvider;
    }

    @Override
    public String getName() {
        return "build";
    }

    @Override
    public void run() {
        contextProvider.get().build();
    }

}
