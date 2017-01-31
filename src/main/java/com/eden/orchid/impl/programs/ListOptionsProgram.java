package com.eden.orchid.impl.programs;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.options.Option;
import com.eden.orchid.options.SiteOptions;
import com.eden.orchid.programs.Program;
import com.eden.orchid.programs.SitePrograms;
import com.eden.orchid.utilities.AutoRegister;

import java.util.Map;

@AutoRegister
public class ListOptionsProgram implements Program {
    @Override
    public String getName() {
        return "listOptions";
    }

    @Override
    public String getDescription() {
        return "Display all possible options for current Orchid build";
    }

    @Override
    public void run() {
        for(Map.Entry<Integer, Option> option : SiteOptions.optionsParsers.entrySet()) {
            Option optionParser = option.getValue();

            if(optionParser.optionLength() > 0) {
                Clog.logger(SitePrograms.loggerKey, "[-#{$1}] (expects #{$2} parameters): #{$3}", new Object[]{optionParser.getFlag(), optionParser.optionLength(), optionParser.getDescription()});
            }
        }
    }
}
