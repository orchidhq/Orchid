package com.eden.orchid.impl.tasks;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.utilities.ObservableTreeSet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class ListCompilersTask extends OrchidTask {

    private Set<OrchidCompiler> compilers;

    @Inject
    public ListCompilersTask(Set<OrchidCompiler> compilers) {
        this.compilers = new ObservableTreeSet<>(compilers);
    }

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
        Clog.logger(null, "" +
                "#{ $0 | fg('cyan') }[Priority]#{$0 |reset}" +
                "#{ $0 | fg('magenta') }[Accepted Input Extensions]#{$0 |reset}" +
                "#{ $0 | fg('yellow') }[Output Extension]#{$0 |reset}" +
                "");
        Clog.logger(null, "------------------------------------------------------------------------------------");
        Clog.logger(null, "------------------------------------------------------------------------------------");

        for (OrchidCompiler compiler : compilers) {

            Clog.logger(null,
                    "#{ $0 | fg('cyan') }   [#{$1}]#{$0 |reset}" +
                            "#{ $0 | fg('magenta') }[#{$2 | join(', ')}]#{$0 |reset}" +
                            "#{ $0 | fg('yellow') }[#{$3}]#{$0 |reset}",
                    compiler.getPriority(), compiler.getSourceExtensions(), compiler.getOutputExtension());

        }
    }
}
