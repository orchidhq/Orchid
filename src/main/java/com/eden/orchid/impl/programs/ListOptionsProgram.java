package com.eden.orchid.impl.programs;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.options.Option;
import com.eden.orchid.programs.Program;
import com.eden.orchid.options.SiteOptions;
import com.eden.orchid.programs.SitePrograms;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.OrchidUtils;

import java.util.Map;

@AutoRegister
public class ListOptionsProgram implements Program {
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
        Clog.logger(SitePrograms.loggerKey, "" +
                "#{ $0 | fg('cyan') }[Priority]#{$0 |reset}" +
                "#{ $0 | fg('magenta') }[Flag]#{$0 |reset}" +
                "#{ $0 | fg('yellow') }[Option Length]#{$0 |reset}");
        Clog.logger(SitePrograms.loggerKey, "------------------------------------------------------------------------------------");
        Clog.logger(SitePrograms.loggerKey, "------------------------------------------------------------------------------------");

        for (Map.Entry<Integer, Option> optionEntry : SiteOptions.optionsParsers.entrySet()) {
            Option option = optionEntry.getValue();

            if (option.optionLength() > 0) {
                Clog.logger(SitePrograms.loggerKey, "" +
                                "#{ $0 | fg('cyan') }[#{$1}]#{$0 |reset}" +
                                "#{ $0 | fg('magenta') }[#{$2}]#{$0 |reset}" +
                                "#{ $0 | fg('yellow') }[#{$3}]#{$0 |reset}",
                        new Object[]{optionEntry.getKey(), "-" + option.getFlag(), option.optionLength() + " parameters"});

                for (String line : OrchidUtils.wrapString(option.getDescription(), 80)) {
                    Clog.logger(SitePrograms.loggerKey, "    " + line);
                }
                Clog.logger(SitePrograms.loggerKey, "    --------------------------------------------------------------------------------");
                Clog.logger(SitePrograms.loggerKey, "");
            }
        }
    }
}
