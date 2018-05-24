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

    Scanner sn;

    @Inject
    public InteractiveTask(Provider<OrchidContext> contextProvider) {
        super(100, "interactive", TaskService.TaskType.OTHER);
        this.contextProvider = contextProvider;
    }

    @Override
    public void run() {
        contextProvider.get().build();

        sn = new Scanner(System.in);
        while(true) {
            System.out.print("Enter a command: \n");
            System.out.print("> ");
            String input = sn.nextLine();
            contextProvider.get().runCommand(input);
        }
    }

    @On(Orchid.Lifecycle.EndSession.class)
    public void onEndSession(Orchid.Lifecycle.EndSession event) {
        sn.close();
    }

}

