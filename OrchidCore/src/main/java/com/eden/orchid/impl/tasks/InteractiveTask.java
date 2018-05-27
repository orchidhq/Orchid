package com.eden.orchid.impl.tasks;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.TaskService;
import com.google.inject.Provider;

import javax.inject.Inject;
import java.util.EventListener;
import java.util.Scanner;

@Description("Starts an interactive shell to run Orchid commands. Exit the interactive session with `quit`.")
public final class InteractiveTask extends OrchidTask implements EventListener {

    private final Provider<OrchidContext> contextProvider;

    private Scanner sn;

    @Inject
    public InteractiveTask(Provider<OrchidContext> contextProvider) {
        super(100, "interactive", TaskService.TaskType.OTHER);
        this.contextProvider = contextProvider;
    }

    @Override
    public void run() {
        contextProvider.get().initOptions();

        sn = new Scanner(System.in);
        while(true) {
            System.out.println("Enter a command:");
            System.out.print("> ");
            contextProvider.get().runCommand(sn.nextLine());
        }
    }

    @On(Orchid.Lifecycle.EndSession.class)
    public void onEndSession(Orchid.Lifecycle.EndSession event) {
        sn.close();
    }

}

