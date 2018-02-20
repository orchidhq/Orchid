package com.eden.orchid.impl.tasks;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.TaskService;
import com.google.inject.Provider;

import javax.inject.Inject;
import java.util.EventListener;

@Description("Makes it easier to create content for your Orchid site by watching your resources for changes and " +
        "rebuilding the site on any changes."
)
public final class WatchTask extends OrchidTask implements EventListener {

    private final Provider<OrchidContext> contextProvider;

    @Inject
    public WatchTask(Provider<OrchidContext> contextProvider) {
        super(100, "watch", TaskService.TaskType.WATCH);
        this.contextProvider = contextProvider;
    }

    @Override
    public void run() {
        contextProvider.get().build();
        contextProvider.get().watch();
    }
}

