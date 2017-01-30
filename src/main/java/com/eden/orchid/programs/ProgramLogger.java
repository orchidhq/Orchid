package com.eden.orchid.programs;

import com.caseyjbrooks.clog.ClogLogger;

public class ProgramLogger implements ClogLogger {
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
        System.out.println(message + " (" + throwable.getMessage() + ")");

        return 0;
    }

    @Override
    public int priority() {
        return 1;
    }
}
