package com.eden.orchid.programs.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.programs.Program;
import com.eden.orchid.programs.SitePrograms;
import com.eden.orchid.utilities.AutoRegister;

import java.util.Map;

@AutoRegister
public class ListProgramsProgram implements Program {
    @Override
    public String getName() {
        return "listPrograms";
    }

    @Override
    public String getDescription() {
        return "Display all available actions for Orchid.";
    }

    @Override
    public void run() {
        for(Map.Entry<String, Program> option : SitePrograms.sitePrograms.entrySet()) {
            Program program = option.getValue();

            Clog.logger(SitePrograms.loggerKey, "[#{$1}]: #{$2}", new Object[]{program.getName(), program.getDescription()});
        }
    }
}
