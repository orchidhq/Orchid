package com.eden.orchid.impl.tasks;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.tasks.OrchidCommand;
import com.google.inject.Provider;

import javax.inject.Inject;

@Description("Run the main Orchid build process.")
public final class QuitCommand extends OrchidCommand {

    private final Provider<OrchidContext> contextProvider;

    @Inject
    public QuitCommand(Provider<OrchidContext> contextProvider) {
        super(1000, "quit");
        this.contextProvider = contextProvider;
    }

    @Override
    public String[] parameters() {
        return new String[0];
    }

    @Override
    public void run(String commandName) {
        contextProvider.get().broadcast(Orchid.Lifecycle.EndSession.fire(this));
    }
}

