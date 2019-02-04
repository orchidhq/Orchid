package com.eden.orchid.impl.compilers.clog;

import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.ClogFormatter;
import com.caseyjbrooks.clog.ClogLogger;
import com.caseyjbrooks.clog.DefaultLogger;
import com.caseyjbrooks.clog.IClog;
import com.caseyjbrooks.clog.parseltongue.Incantation;
import com.caseyjbrooks.clog.parseltongue.Parseltongue;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEventListener;
import com.google.inject.Provider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
public final class ClogSetupListener implements OrchidEventListener {

    private final Provider<OrchidContext> contextProvider;
    private final Provider<Set<TemplateFunction>> templateTagsProvider ;

    private final WarningLogCollector warningLogger = new WarningLogCollector();
    private final ErrorLogCollector errorLogger = new ErrorLogCollector();
    private final FatalLogCollector fatalLogger = new FatalLogCollector();

    @Inject
    public ClogSetupListener(Provider<OrchidContext> contextProvider, Provider<Set<TemplateFunction>> templateTagsProvider) {
        this.contextProvider = contextProvider;
        this.templateTagsProvider = templateTagsProvider;
    }

    @On(Orchid.Lifecycle.InitComplete.class)
    public void onStart(Orchid.Lifecycle.InitComplete event) {
        Clog.getInstance().addLogger(Clog.KEY_W, warningLogger);
        Clog.getInstance().addLogger(Clog.KEY_E, errorLogger);
        Clog.getInstance().addLogger(Clog.KEY_WTF, fatalLogger);

        ClogFormatter formatter = Clog.getInstance().getFormatter();
        if (formatter instanceof Parseltongue) {
            Parseltongue pt = (Parseltongue) formatter;
            pt.findSpells(ClogSpells.class);

            List<Incantation> incantations = templateTagsProvider.get()
                    .stream()
                    .map((templateTag) -> new ClogIncantationWrapper(
                            contextProvider,
                            templateTag.getName(),
                            Arrays.asList(templateTag.parameters()),
                            templateTag.getClass()
                    ))
                    .collect(Collectors.toList());

            Incantation[] incantationsArray = new Incantation[incantations.size()];
            incantations.toArray(incantationsArray);

            pt.addSpells(incantationsArray);
        }
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

    public static void registerJavaLoggingHandler() {
        //reset() will remove all default handlers
        LogManager.getLogManager().reset();
        Logger rootLogger = LogManager.getLogManager().getLogger("");

        // add our custom Java Logging handler
        rootLogger.addHandler(new ClogJavaLoggingHandler());

        // ignore annoying Hibernate Validator message
        Clog.getInstance().addTagToBlacklist("org.hibernate.validator.internal.util.Version");
    }

    private static class ClogJavaLoggingHandler extends Handler {

        @Override
        public void publish(LogRecord record) {
            Level level = record.getLevel();
            int levelInt = level.intValue();

            IClog clog = Clog.tag(record.getSourceClassName());

            if(level.equals(Level.OFF)) {
                // do nothing
            }
            else if(level.equals(Level.ALL)) {
                // always log at verbose level
                clog.v(record.getMessage());
            }
            else {
                // log at closest Clog level
                if(levelInt >= Level.SEVERE.intValue()) {
                    clog.e(record.getMessage());
                }
                else if(levelInt >= Level.WARNING.intValue()) {
                    clog.w(record.getMessage());
                }
                else if(levelInt >= Level.INFO.intValue()) {
                    clog.i(record.getMessage());
                }
                else if(levelInt >= Level.CONFIG.intValue()) {
                    clog.d(record.getMessage());
                }
                else {
                    clog.v(record.getMessage());
                }
            }
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }

}
