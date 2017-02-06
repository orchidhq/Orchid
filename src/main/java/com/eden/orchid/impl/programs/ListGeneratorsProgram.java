package com.eden.orchid.impl.programs;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.generators.Generator;
import com.eden.orchid.programs.Program;
import com.eden.orchid.generators.SiteGenerators;
import com.eden.orchid.programs.SitePrograms;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.OrchidUtils;

import java.util.Map;

@AutoRegister
public class ListGeneratorsProgram implements Program {
    @Override
    public String getName() {
        return "listGenerators";
    }

    @Override
    public String getDescription() {
        return "Display all available Generators that are can be used to create Orchid site pages.";
    }

    @Override
    public void run() {
        Clog.logger(SitePrograms.loggerKey, "" +
                "#{ $0 | fg('cyan') }[Priority]#{$0 |reset}" +
                "#{ $0 | fg('magenta') }[Name]#{$0 |reset}");
        Clog.logger(SitePrograms.loggerKey, "------------------------------------------------------------------------------------");
        Clog.logger(SitePrograms.loggerKey, "------------------------------------------------------------------------------------");

        for (Map.Entry<Integer, Generator> generatorEntry : SiteGenerators.generators.entrySet()) {
            Generator generator = generatorEntry.getValue();

            if (SiteGenerators.shouldUseGenerator(generator)) {
                Clog.logger(SitePrograms.loggerKey, "" +
                                "#{ $0 | fg('cyan') }[#{$1}]#{$0 |reset}" +
                                "#{ $0 | fg('magenta') }[#{$2}]#{$0 |reset}",
                        new Object[]{generatorEntry.getKey(), generator.getClass().getName()});
            }
            else {
                Clog.logger(SitePrograms.loggerKey, "" +
                                "#{ $0 | fg('cyan') }[#{$1}]#{$0 |reset}" +
                                "#{ $0 | fg('red') }[#{$2} (disabled)]#{$0 |reset}",
                        new Object[]{generatorEntry.getKey(), generator.getClass().getName()});
            }

            for (String line : OrchidUtils.wrapString(generator.getDescription(), 80)) {
                Clog.logger(SitePrograms.loggerKey, "    " + line);
            }
            Clog.logger(SitePrograms.loggerKey, "    --------------------------------------------------------------------------------");
            Clog.logger(SitePrograms.loggerKey, "");
        }
    }
}
