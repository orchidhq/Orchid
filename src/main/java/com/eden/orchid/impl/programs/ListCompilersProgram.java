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
        return "Display all available compilers that can be used to process content";
    }

    @Override
    public void run() {
        for(Map.Entry<Integer, Compiler> compilerEntry : SiteCompilers.compilers.entrySet()) {
            Compiler compiler = compilerEntry.getValue();

            Clog.logger(SitePrograms.loggerKey, "[#{$1}][#{ $2 | join(', ') }]", new Object[]{ compiler.getClass().getSimpleName(), compiler.getSourceExtensions() });
        }
    }
}
