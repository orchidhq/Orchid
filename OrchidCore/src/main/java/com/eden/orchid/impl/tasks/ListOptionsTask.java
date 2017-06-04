package com.eden.orchid.impl.tasks;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.OrchidTasks;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Singleton;

@Singleton
public class ListOptionsTask extends OrchidTask {

    @Override
    public String getName() {
        return "listOptions";
    }

    @Override
    public String getDescription() {
        return "Display all available Options for the currently registered components.";
    }

    @Override
    public void run() {
        Clog.logger(OrchidTasks.loggerKey, "" +
                "#{ $0 | fg('magenta') }[Flag]#{$0 |reset}" +
                "#{ $0 | fg('yellow') }[OrchidOption Length]#{$0 |reset}");
        Clog.logger(OrchidTasks.loggerKey, "------------------------------------------------------------------------------------");
        Clog.logger(OrchidTasks.loggerKey, "------------------------------------------------------------------------------------");

        for (OrchidFlag option : OrchidFlags.getInstance().getFlags()) {

            if (option.optionLength() > 0) {
                Clog.logger(OrchidTasks.loggerKey, "" +
                                "#{ $0 | fg('magenta') }[#{$2}]#{$0 |reset}" +
                                "#{ $0 | fg('yellow') }[#{$3}]#{$0 |reset}",
                        new Object[]{"-" + option.getFlag(), option.optionLength() + " parameters"});

                for (String line : OrchidUtils.wrapString(option.getDescription(), 80)) {
                    Clog.logger(OrchidTasks.loggerKey, "    " + line);
                }
                Clog.logger(OrchidTasks.loggerKey, "    --------------------------------------------------------------------------------");
                Clog.logger(OrchidTasks.loggerKey, "");
            }
        }
    }
}
