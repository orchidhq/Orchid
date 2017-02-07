package com.eden.orchid.impl.programs;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.Theme;
import com.eden.orchid.programs.Program;
import com.eden.orchid.programs.SitePrograms;
import com.eden.orchid.resources.ResourceSource;
import com.eden.orchid.utilities.AutoRegister;

import java.util.Map;

@AutoRegister
public class ListThemesProgram implements Program {

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
        Clog.logger(SitePrograms.loggerKey, "" +
                "#{ $0 | fg('cyan') }[Priority]#{$0 |reset}" +
                "#{ $0 | fg('magenta') }[Theme]#{$0 |reset}" +
                "#{ $0 | fg('blue') }[Theme Parent]#{$0 |reset}" +
                "");
        Clog.logger(SitePrograms.loggerKey, "------------------------------------------------------------------------------------");
        Clog.logger(SitePrograms.loggerKey, "------------------------------------------------------------------------------------");

        for(Map.Entry<Integer, ResourceSource> resourceSourceEntry : Orchid.getResources().getResourceSources().entrySet()) {
            if (resourceSourceEntry.getValue() instanceof Theme) {
                Theme theme = (Theme) resourceSourceEntry.getValue();

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

                Clog.logger(SitePrograms.loggerKey, message, new Object[]{theme.getResourcePriority(), theme.getClass().getName(), theme.getClass().getSuperclass().getSimpleName()});
            }
        }
    }
}
