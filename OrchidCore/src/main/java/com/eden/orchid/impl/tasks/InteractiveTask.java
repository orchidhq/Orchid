package com.eden.orchid.impl.tasks;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.tasks.OrchidTask;
import com.google.inject.Provider;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.EventListener;
import java.util.Scanner;

public final class InteractiveTask extends OrchidTask implements EventListener {

    private final Provider<OrchidContext> contextProvider;

    @Inject
    public InteractiveTask(Provider<OrchidContext> contextProvider) {
        super(100);
        this.contextProvider = contextProvider;
    }

    @Override
    public String getName() {
        return "i";
    }

    @Override
    public String getDescription() {
        return "Makes it easier to create content for your Orchid site by watching your resources for changes and " +
                "rebuilding the site on any changes.";
    }

    @Override
    public void run() {
        Scanner sn = new Scanner(System.in);
        while(true) {
            System.out.print("Enter a command: \n");
            System.out.print("> ");
            String input = sn.nextLine();
            String[] inputPieces = input.split("\\s+");
            String command = inputPieces[0];
            String params = String.join(" ", Arrays.copyOfRange(inputPieces, 1, inputPieces.length));

            if(command.equalsIgnoreCase("quit")) {
                contextProvider.get().broadcast(Orchid.Lifecycle.EndSession.fire(this));
                break;
            }
            else {
                contextProvider.get().runCommand(command, params);
            }
        }
        sn.close();
    }
}

