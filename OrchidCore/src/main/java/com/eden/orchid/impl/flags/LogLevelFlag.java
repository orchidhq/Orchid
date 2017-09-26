package com.eden.orchid.impl.flags;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.options.OrchidFlag;

public final class LogLevelFlag implements OrchidFlag {

    @Override
    public String getFlag() {
        return "logLevel";
    }

    @Override
    public String getDescription() {
        return "The level of logging statements to show. One of: [VERBOSE, DEBUG, INFO, DEFAULT, WARNING, ERROR, FATAL]";
    }

    @Override
    public Object parseFlag(String[] options) {
        Clog.setMinPriority(Clog.Priority.getByKey(options[1]));
        return options[1];
    }
}
