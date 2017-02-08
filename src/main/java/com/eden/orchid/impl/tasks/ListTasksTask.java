package com.eden.orchid.impl.tasks;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.OrchidTasks;
import com.eden.orchid.api.registration.AutoRegister;
import com.eden.orchid.utilities.OrchidUtils;

import java.util.Map;

@AutoRegister
public class ListTasksTask implements OrchidTask {
    @Override
    public String getName() {
        return "listTasks";
    }

    @Override
    public String getDescription() {
        return "Display all available Tasks that can be run.";
    }

    @Override
    public void run() {
        Clog.logger(OrchidTasks.loggerKey, "" +
                "#{ $0 | fg('magenta') }[OrchidTask Name]#{$0 |reset}" +
                "\n------------------------------------------------------------------------------------" +
                "\n------------------------------------------------------------------------------------");

        for(Map.Entry<String, OrchidTask> taskEntry : getRegistrar().getSiteTasks().entrySet()) {
            OrchidTask task = taskEntry.getValue();

            Clog.logger(OrchidTasks.loggerKey, "#{ $0 | fg('magenta') }[#{$1}]#{$0 |reset}", new Object[]{ task.getName() });

            for(String line : OrchidUtils.wrapString(task.getDescription(), 80)) {
                Clog.logger(OrchidTasks.loggerKey, "    " + line);
            }
            Clog.logger(OrchidTasks.loggerKey, "    --------------------------------------------------------------------------------");
            Clog.logger(OrchidTasks.loggerKey, "");
        }
    }
}
