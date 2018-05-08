package com.eden.orchid.impl.tasks;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.TaskService;
import com.google.inject.Provider;

import javax.inject.Inject;
import javax.inject.Named;

@Description("Run the main Orchid build process, publish the results, then exit.")
public final class DeployTask extends OrchidTask {

    private final Provider<OrchidContext> contextProvider;
    private final boolean dryDeploy;

    @Inject
    public DeployTask(Provider<OrchidContext> contextProvider, @Named("dryDeploy") Boolean dryDeploy) {
        super(100, "deploy", TaskService.TaskType.DEPLOY);
        this.contextProvider = contextProvider;
        this.dryDeploy = dryDeploy;
    }

    @Override
    public void run() {
        contextProvider.get().build();
        contextProvider.get().deploy(dryDeploy);
    }

}
