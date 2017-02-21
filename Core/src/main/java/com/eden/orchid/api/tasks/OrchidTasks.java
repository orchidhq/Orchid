package com.eden.orchid.api.tasks;

import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.ClogLogger;
import com.eden.orchid.utilities.ObservableTreeSet;
import org.fusesource.jansi.AnsiConsole;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@Singleton
public final class OrchidTasks {

    public static String defaultTask = "build";
    public static final String loggerKey = "clear";

    private Set<OrchidTask> tasks;

    @Inject
    public OrchidTasks(Set<OrchidTask> tasks) {
        Clog.addLogger(loggerKey, new TaskLogger());
        this.tasks = new ObservableTreeSet<>(tasks);
    }

    public void run(String taskName) {
        OrchidTask foundTask = Arrays
                .stream((new String[]{taskName, defaultTask, "build"}))
                .map(t ->
                    tasks
                        .stream()
                        .filter(task -> task.getName().equals(t))
                        .findFirst()
                        .orElseGet(() -> null)
                )
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> null);

        if(foundTask != null) {
            foundTask.run();
        }
    }

    public static class TaskLogger implements ClogLogger {
        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public int log(String tag, String message) {
            System.out.println(message);

            return 0;
        }

        @Override
        public int log(String tag, String message, Throwable throwable) {
            AnsiConsole.out.println(message + " (" + throwable.getMessage() + ")");

            return 0;
        }

        @Override
        public int priority() {
            return 1;
        }
    }
}
