package com.eden.orchid.impl.programs;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.compilers.Compiler;
import com.eden.orchid.compilers.SiteCompilers;
import com.eden.orchid.programs.Program;
import com.eden.orchid.programs.SitePrograms;
import com.eden.orchid.utilities.AutoRegister;

import java.util.Map;

@AutoRegister
public class ListCompilersProgram implements Program {
    @Override
    public String getName() {
        return "listCompilers";
    }

    @Override
    public String getDescription() {
        return "Display all available Compilers that can be used to process content.";
    }

    @Override
    public void run() {
        Clog.logger(SitePrograms.loggerKey, "" +
                "#{ $0 | fg('cyan') }[Priority]#{$0 |reset}" +
                "#{ $0 | fg('magenta') }[Accepted Input Extensions]#{$0 |reset}" +
                "#{ $0 | fg('yellow') }[Output Extension]#{$0 |reset}" +
                "");
        Clog.logger(SitePrograms.loggerKey, "------------------------------------------------------------------------------------");
        Clog.logger(SitePrograms.loggerKey, "------------------------------------------------------------------------------------");

        for (Map.Entry<Integer, Compiler> compilerEntry : SiteCompilers.compilers.entrySet()) {
            Compiler compiler = compilerEntry.getValue();

            Clog.logger(SitePrograms.loggerKey,
                    "#{ $0 | fg('cyan') }   [#{$1}]#{$0 |reset}" +
                            "#{ $0 | fg('magenta') }[#{$2 | join(', ')}]#{$0 |reset}" +
                            "#{ $0 | fg('yellow') } [#{$3}]#{$0 |reset}",
                    new Object[]{compilerEntry.getKey(), compiler.getSourceExtensions(), compiler.getOutputExtension()});

        }
    }
}
