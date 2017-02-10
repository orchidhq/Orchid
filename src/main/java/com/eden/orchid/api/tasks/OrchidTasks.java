package com.eden.orchid.api.tasks;

import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.ClogLogger;
import com.eden.orchid.api.registration.Contextual;
import org.fusesource.jansi.AnsiConsole;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;
import java.util.TreeSet;

@Singleton
public class OrchidTasks implements Contextual {

    public static final String defaultTask = "build";
    public static final String loggerKey = "clear";

    private Set<OrchidTask> tasks;

    @Inject
    public OrchidTasks(Set<OrchidTask> tasks) {
        Clog.addLogger(loggerKey, new TaskLogger());
        this.tasks = new TreeSet<>(tasks);
    }

    public void run(String taskName) {
        for(OrchidTask task : tasks) {
            if(task.getName().equals(taskName)) {
                task.run();
                return;
            }
        }

        for(OrchidTask task : tasks) {
            if(task.getName().equals(defaultTask)) {
                task.run();
                return;
            }
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
