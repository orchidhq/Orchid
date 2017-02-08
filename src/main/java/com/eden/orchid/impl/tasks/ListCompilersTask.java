package com.eden.orchid.impl.tasks;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.registration.AutoRegister;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.OrchidTasks;

import java.util.Map;

@AutoRegister
public class ListCompilersTask implements OrchidTask {
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
        Clog.logger(OrchidTasks.loggerKey, "" +
                "#{ $0 | fg('cyan') }[Priority]#{$0 |reset}" +
                "#{ $0 | fg('magenta') }[Accepted Input Extensions]#{$0 |reset}" +
                "#{ $0 | fg('yellow') }[Output Extension]#{$0 |reset}" +
                "");
        Clog.logger(OrchidTasks.loggerKey, "------------------------------------------------------------------------------------");
        Clog.logger(OrchidTasks.loggerKey, "------------------------------------------------------------------------------------");

        for (Map.Entry<Integer, OrchidCompiler> compilerEntry : getRegistrar().resolveMap(OrchidCompiler.class).entrySet()) {
            OrchidCompiler compiler = compilerEntry.getValue();

            Clog.logger(OrchidTasks.loggerKey,
                    "#{ $0 | fg('cyan') }   [#{$1}]#{$0 |reset}" +
                            "#{ $0 | fg('magenta') }[#{$2 | join(', ')}]#{$0 |reset}" +
                            "#{ $0 | fg('yellow') }[#{$3}]#{$0 |reset}",
                    new Object[]{compilerEntry.getKey(), compiler.getSourceExtensions(), compiler.getOutputExtension()});

        }
    }
}
