package com.eden.orchid.impl.events;

import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.ClogLogger;
import com.caseyjbrooks.clog.DefaultLogger;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEventListener;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Singleton
public class ClogSetupListener implements OrchidEventListener {

    private final WarningLogCollector warningLogger = new WarningLogCollector();
    private final ErrorLogCollector errorLogger = new ErrorLogCollector();
    private final FatalLogCollector fatalLogger = new FatalLogCollector();

    @On(Orchid.Lifecycle.OnStart.class)
    public void onStart(Orchid.Lifecycle.OnStart event) {
        Clog.getInstance().addLogger(Clog.KEY_W, warningLogger);
        Clog.getInstance().addLogger(Clog.KEY_E, errorLogger);
        Clog.getInstance().addLogger(Clog.KEY_WTF, fatalLogger);
    }

    @On(Orchid.Lifecycle.BuildFinish.class)
    public void onBuildFinish(Orchid.Lifecycle.BuildFinish event) {
        printPendingMessages();
    }

    @On(Orchid.Lifecycle.DeployFinish.class)
    public void onBuildFinish(Orchid.Lifecycle.DeployFinish deployFinish) {
        printPendingMessages();
    }

    @On(Orchid.Lifecycle.Shutdown.class)
    public void onShutdown(Orchid.Lifecycle.Shutdown event) {
        printPendingMessages();
    }

    private void printPendingMessages() {
        warningLogger.printAllMessages();
        errorLogger.printAllMessages();
        fatalLogger.printAllMessages();
    }

    private static class WarningLogCollector extends AbstractLogCollector {

        @Override
        public Clog.Priority priority() {
            return Clog.Priority.WARNING;
        }

        @Override
        String getHeaderMessage() {
            return "Warnings:";
        }
    }

    private static class ErrorLogCollector extends AbstractLogCollector {

        @Override
        public Clog.Priority priority() {
            return Clog.Priority.ERROR;
        }

        @Override
        String getHeaderMessage() {
            return "Errors:";
        }
    }

    private static class FatalLogCollector extends AbstractLogCollector {

        @Override
        public Clog.Priority priority() {
            return Clog.Priority.FATAL;
        }

        @Override
        String getHeaderMessage() {
            return "Fatal exceptions:";
        }
    }

    private static abstract class AbstractLogCollector implements ClogLogger {
        static final class LogMessage {
            final String message;
            final Throwable throwable;

            LogMessage(String message, Throwable throwable) {
                this.message = message;
                this.throwable = throwable;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                LogMessage that = (LogMessage) o;
                return Objects.equals(message, that.message) &&
                        Objects.equals(throwable, that.throwable);
            }

            @Override
            public int hashCode() {
                return Objects.hash(message, throwable);
            }
        }

        final Map<String, Set<LogMessage>> messages = new HashMap<>();

        @Override
        public boolean isActive() {
            return true;
        }

        @Override
        public int log(String tag, String message) {
            messages.computeIfAbsent(tag, s -> new HashSet<>());
            messages.get(tag).add(new LogMessage(message, null));
            if(!Orchid.getInstance().getState().isWorkingState()) {
                printAllMessages();
            }

            return 0;
        }

        @Override
        public int log(String tag, String message, Throwable throwable) {
            messages.computeIfAbsent(tag, s -> new HashSet<>());
            messages.get(tag).add(new LogMessage(message, throwable));
            if(!Orchid.getInstance().getState().isWorkingState()) {
                printAllMessages();
            }

            return 0;
        }

        abstract String getHeaderMessage();

        void printAllMessages() {
            if(messages.size() > 0) {
                DefaultLogger logger = new DefaultLogger(priority());

                logger.log("", getHeaderMessage());

                messages.forEach((tag, logMessages) -> {
                    logger.log(tag, "");
                    logMessages.forEach(message -> {
                        if (message.throwable != null) {
                            logger.log("", "    - " + message.message, message.throwable);
                        } else {
                            logger.log("", "    - " + message.message);
                        }
                    });
                    System.out.println("");
                });

                messages.clear();
                System.out.println("");
            }
        }
    }

}
