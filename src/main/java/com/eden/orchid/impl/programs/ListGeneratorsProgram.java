package com.eden.orchid.impl.programs;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.generators.Generator;
import com.eden.orchid.generators.SiteGenerators;
import com.eden.orchid.programs.Program;
import com.eden.orchid.programs.SitePrograms;
import com.eden.orchid.utilities.AutoRegister;

import java.util.Map;

@AutoRegister
public class ListGeneratorsProgram implements Program {
    @Override
    public String getName() {
        return "listGenerators";
    }

    @Override
    public String getDescription() {
        return "Display all available generators that are used to index and create content";
    }

    @Override
    public void run() {
        for(Map.Entry<Integer, Generator> generatorEntry : SiteGenerators.generators.entrySet()) {
            Generator generator = generatorEntry.getValue();

            if(SiteGenerators.shouldUseGenerator(generator)) {
                Clog.logger(SitePrograms.loggerKey, "[#{$1}]", new Object[]{ generator.getClass().getName() });
            }
            else {
                Clog.logger(SitePrograms.loggerKey, "[#{$1}] (currently disabled)", new Object[]{ generator.getClass().getName() });
            }
        }
    }
}
