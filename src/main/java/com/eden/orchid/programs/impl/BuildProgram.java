package com.eden.orchid.programs.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.programs.Program;
import com.eden.orchid.utilities.AutoRegister;

@AutoRegister
public class BuildProgram implements Program {
    @Override
    public String getName() {
        return "build";
    }

    @Override
    public String getDescription() {
        return "Build the Orchid site.";
    }

    @Override
    public void run() {
        Clog.i("Running #{$1}", new Object[]{this.getClass().getSimpleName()});
        Orchid.indexingScan();
        Orchid.generationScan();
        Orchid.generateHomepage();
    }
}
