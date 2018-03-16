package com.eden.orchid.impl.tasks;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.tasks.OrchidCommand;
import com.google.inject.Provider;

import javax.inject.Inject;

@Description("Run the main Orchid build process.")
public final class BuildCommand extends OrchidCommand {

    private final Provider<OrchidContext> contextProvider;

    @Inject
    public BuildCommand(Provider<OrchidContext> contextProvider) {
        super(100, "build");
        this.contextProvider = contextProvider;
    }

    @Override
    public String[] parameters() {
        return new String[0];
    }

    @Override
    public void run(String commandName) throws Exception {
        contextProvider.get().build();
    }
}

