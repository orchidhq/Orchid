package com.eden.orchid.api.tasks;

import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.ClogLogger;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.events.EventServiceImpl;
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
    private EventServiceImpl emitter;

    @Inject
    public OrchidTasks(Set<OrchidTask> tasks, EventServiceImpl emitter) {
        Clog.addLogger(loggerKey, new TaskLogger());
        this.tasks = new ObservableTreeSet<>(tasks);
        this.emitter = emitter;
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
            emitter.broadcast(Orchid.Events.TASK_START, foundTask);
            foundTask.run();
            emitter.broadcast(Orchid.Events.TASK_FINISH, foundTask);
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
