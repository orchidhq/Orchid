package com.eden.orchid.impl.programs;

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
        return "Run the main Orchid build process. This is the default Program for Javadoc and if no other program is specified.";
    }

    @Override
    public void run() {
        Orchid.build();
    }
}
