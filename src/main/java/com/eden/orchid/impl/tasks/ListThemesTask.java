package com.eden.orchid.impl.tasks;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Theme;
import com.eden.orchid.api.registration.AutoRegister;
import com.eden.orchid.api.resources.OrchidResourceSource;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.OrchidTasks;

@AutoRegister
public class ListThemesTask implements OrchidTask {

    @Override
    public String getName() {
        return "listThemes";
    }

    @Override
    public String getDescription() {
        return "Display all available themes, in the order in which their resources are used. Themes that are not the " +
                "currently selected theme or a parent of the current theme are disabled, and their resources associated" +
                "resources are ignored.";
    }

    @Override
    public void run() {
        Clog.logger(OrchidTasks.loggerKey, "" +
                "#{ $0 | fg('cyan') }[Priority]#{$0 |reset}" +
                "#{ $0 | fg('magenta') }[Theme]#{$0 |reset}" +
                "#{ $0 | fg('blue') }[Theme Parent]#{$0 |reset}" +
                "");
        Clog.logger(OrchidTasks.loggerKey, "------------------------------------------------------------------------------------");
        Clog.logger(OrchidTasks.loggerKey, "------------------------------------------------------------------------------------");

        for(OrchidResourceSource resourceSourceEntry : getRegistrar().resolveSet(OrchidResourceSource.class)) {
            if (resourceSourceEntry instanceof Theme) {
                Theme theme = (Theme) resourceSourceEntry;

                String message;

                if (theme.getResourcePriority() < 0) {
                    message = "#{ $0 | fg('red') }[#{$1}][#{$2}]";
                    if (!theme.getClass().getSuperclass().equals(Theme.class)) {
                        message += "[#{$3}]";
                    }
                    message += "(inactive)#{$0 |reset}";
                }
                else {
                    message = "#{ $0 | fg('cyan') }[#{$1}]#{$0 |reset}";
                    message += "#{ $0 | fg('magenta') }[#{$2}]#{$0 |reset}";
                    if (!theme.getClass().getSuperclass().equals(Theme.class)) {
                        message += "#{ $0 | fg('blue') }[#{$3}]#{$0 |reset}";
                    }
                }

                Clog.logger(OrchidTasks.loggerKey, message, new Object[]{theme.getResourcePriority(), theme.getClass().getName(), theme.getClass().getSuperclass().getSimpleName()});
            }
        }
    }
}
