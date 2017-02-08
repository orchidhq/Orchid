package com.eden.orchid.impl.tasks;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.registration.AutoRegister;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.OrchidTasks;
import com.eden.orchid.utilities.OrchidUtils;

@AutoRegister
public class ListGeneratorsTask implements OrchidTask {
    @Override
    public String getName() {
        return "listGenerators";
    }

    @Override
    public String getDescription() {
        return "Display all available Generators that are can be used to create Orchid site pages.";
    }

    @Override
    public void run() {
        Clog.logger(OrchidTasks.loggerKey, "" +
                "#{ $0 | fg('cyan') }[Priority]#{$0 |reset}" +
                "#{ $0 | fg('magenta') }[Name]#{$0 |reset}");
        Clog.logger(OrchidTasks.loggerKey, "------------------------------------------------------------------------------------");
        Clog.logger(OrchidTasks.loggerKey, "------------------------------------------------------------------------------------");

        for (OrchidGenerator generator : getRegistrar().resolveSet(OrchidGenerator.class)) {

            if (getContext().getGenerators().shouldUseGenerator(generator)) {
                Clog.logger(OrchidTasks.loggerKey, "" +
                                "#{ $0 | fg('cyan') }[#{$1}]#{$0 |reset}" +
                                "#{ $0 | fg('magenta') }[#{$2}]#{$0 |reset}",
                        new Object[]{generator.priority(), generator.getClass().getName()});
            }
            else {
                Clog.logger(OrchidTasks.loggerKey, "" +
                                "#{ $0 | fg('cyan') }[#{$1}]#{$0 |reset}" +
                                "#{ $0 | fg('red') }[#{$2} (disabled)]#{$0 |reset}",
                        new Object[]{generator.priority(), generator.getClass().getName()});
            }

            for (String line : OrchidUtils.wrapString(generator.getDescription(), 80)) {
                Clog.logger(OrchidTasks.loggerKey, "    " + line);
            }
            Clog.logger(OrchidTasks.loggerKey, "    --------------------------------------------------------------------------------");
            Clog.logger(OrchidTasks.loggerKey, "");
        }
    }
}
