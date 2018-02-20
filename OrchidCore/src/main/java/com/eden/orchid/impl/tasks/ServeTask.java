package com.eden.orchid.impl.tasks;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.TaskService;
import com.google.inject.Provider;

import javax.inject.Inject;

@Description("Makes it easier to create content for your Orchid site by watching your resources for changes and " +
        "rebuilding the site on any changes. A static HTTP server is also created in the root of your site and " +
        "the baseUrl set to this server's address so you can preview the output. You can also access the admin " +
        "dashboard to get insight into your current Orchid setup and manage your content."
)
public final class ServeTask extends OrchidTask {

    private final Provider<OrchidContext> contextProvider;

    @Inject
    public ServeTask(Provider<OrchidContext> contextProvider) {
        super(100, "serve", TaskService.TaskType.SERVE);
        this.contextProvider = contextProvider;
    }

    @Override
    public void run() {
        contextProvider.get().serve();
        contextProvider.get().build();
        contextProvider.get().watch();
    }

}

