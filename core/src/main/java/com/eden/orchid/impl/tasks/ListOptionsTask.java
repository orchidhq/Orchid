package com.eden.orchid.impl.tasks;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.options.OrchidOption;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.OrchidTasks;
import com.eden.orchid.utilities.ObservableTreeSet;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class ListOptionsTask extends OrchidTask {

    private Set<OrchidOption> options;

    @Inject
    public ListOptionsTask(Set<OrchidOption> options) {
        this.options = new ObservableTreeSet<>(options);
    }

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
                "#{ $0 | fg('cyan') }[Priority]#{$0 |reset}" +
                "#{ $0 | fg('magenta') }[Flag]#{$0 |reset}" +
                "#{ $0 | fg('yellow') }[OrchidOption Length]#{$0 |reset}");
        Clog.logger(OrchidTasks.loggerKey, "------------------------------------------------------------------------------------");
        Clog.logger(OrchidTasks.loggerKey, "------------------------------------------------------------------------------------");

        for (OrchidOption option : options) {

            if (option.optionLength() > 0) {
                Clog.logger(OrchidTasks.loggerKey, "" +
                                "#{ $0 | fg('cyan') }[#{$1}]#{$0 |reset}" +
                                "#{ $0 | fg('magenta') }[#{$2}]#{$0 |reset}" +
                                "#{ $0 | fg('yellow') }[#{$3}]#{$0 |reset}",
                        new Object[]{option.getPriority(), "-" + option.getFlag(), option.optionLength() + " parameters"});

                for (String line : OrchidUtils.wrapString(option.getDescription(), 80)) {
                    Clog.logger(OrchidTasks.loggerKey, "    " + line);
                }
                Clog.logger(OrchidTasks.loggerKey, "    --------------------------------------------------------------------------------");
                Clog.logger(OrchidTasks.loggerKey, "");
            }
        }
    }
}
