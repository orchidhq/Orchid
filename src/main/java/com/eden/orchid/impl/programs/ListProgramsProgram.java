package com.eden.orchid.impl.programs;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.programs.Program;
import com.eden.orchid.programs.SitePrograms;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.OrchidUtils;

import java.util.Map;

@AutoRegister
public class ListProgramsProgram implements Program {
    @Override
    public String getName() {
        return "listPrograms";
    }

    @Override
    public String getDescription() {
        return "Display all available Programs that can be run.";
    }

    @Override
    public void run() {
        Clog.logger(SitePrograms.loggerKey, "" +
                "#{ $0 | fg('magenta') }[Program Name]#{$0 |reset}" +
                "\n------------------------------------------------------------------------------------" +
                "\n------------------------------------------------------------------------------------");

        for(Map.Entry<String, Program> programEntry : SitePrograms.sitePrograms.entrySet()) {
            Program program = programEntry.getValue();

            Clog.logger(SitePrograms.loggerKey, "#{ $0 | fg('magenta') }[#{$1}]#{$0 |reset}", new Object[]{ program.getName() });

            for(String line : OrchidUtils.wrapString(program.getDescription(), 80)) {
                Clog.logger(SitePrograms.loggerKey, "    " + line);
            }
            Clog.logger(SitePrograms.loggerKey, "    --------------------------------------------------------------------------------");
            Clog.logger(SitePrograms.loggerKey, "");
        }
    }
}
