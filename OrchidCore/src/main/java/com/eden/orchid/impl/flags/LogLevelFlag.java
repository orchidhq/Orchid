package com.eden.orchid.impl.flags;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.annotations.Description;

@Description("The level of logging statements to show. One of: [VERBOSE, DEBUG, INFO, DEFAULT, WARNING, ERROR, FATAL]")
public final class LogLevelFlag extends OrchidFlag {

    public LogLevelFlag() {
        super("logLevel", false, null);
    }

    @Override
    public Object parseFlag(String[] options) {
        Clog.getInstance().setMinPriority(Clog.Priority.getByKey(options[1]));
        return options[1];
    }

}
