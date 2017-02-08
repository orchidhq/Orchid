package com.eden.orchid.api.tasks;

import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.ClogLogger;
import com.eden.orchid.api.registration.Contextual;
import org.fusesource.jansi.AnsiConsole;

public class OrchidTasks implements Contextual {

    public static final String defaultTask = "build";
    public static final String loggerKey = "clear";

    public OrchidTasks() {
        Clog.addLogger(loggerKey, new TaskLogger());
    }

    public void run(String taskName) {
        if (getRegistrar().getSiteTasks().containsKey(taskName)) {
            getRegistrar().getSiteTasks().get(taskName).run();
        }
        else {
            getRegistrar().getSiteTasks().get(defaultTask).run();
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
