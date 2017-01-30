package com.eden.orchid.programs.impl;

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
        Orchid.build();
    }
}
