package com.eden.orchid.programs;

import com.caseyjbrooks.clog.Clog;
import com.caseyjbrooks.clog.ClogLogger;
import com.eden.orchid.utilities.RegistrationProvider;
import org.fusesource.jansi.AnsiConsole;

import java.util.HashMap;
import java.util.Map;

public class SitePrograms implements RegistrationProvider {

    public static Map<String, Program> sitePrograms = new HashMap<>();
    public static String defaultProgram = "build";
    public static String loggerKey = "clear";

    static {
        Clog.addLogger(loggerKey, new ProgramLogger());
    }

    @Override
    public void register(Object object) {
        if (object instanceof Program) {
            Program program = (Program) object;

            sitePrograms.put(program.getName(), program);
        }
    }

    public static void runProgram(String programName) {
        if (sitePrograms.containsKey(programName)) {
            sitePrograms.get(programName).run();
        }
        else {
            sitePrograms.get(defaultProgram).run();
        }
    }

    public static class ProgramLogger implements ClogLogger {
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
