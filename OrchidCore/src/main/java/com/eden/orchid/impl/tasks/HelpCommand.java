package com.eden.orchid.impl.tasks;

import com.caseyjbrooks.clog.Clog;
import com.eden.krow.KrowTable;
import com.eden.krow.formatters.HtmlTableFormatter;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionHolderDescription;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.server.OrchidServer;
import com.eden.orchid.api.tasks.OrchidCommand;
import com.google.inject.Provider;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public final class HelpCommand extends OrchidCommand {

    private final Provider<OrchidContext> contextProvider;
    private final OrchidServer server;

    @Getter @Setter
    @Option
    @Description("The fully-qualified name of a class to describe. This will show a table will all the available " +
            "options, along with other relevant data about that class."
    )
    private String className;

    @Inject
    public HelpCommand(Provider<OrchidContext> contextProvider, OrchidServer server) {
        super(100, "describe");
        this.contextProvider = contextProvider;
        this.server = server;
    }

    @Override
    public String[] parameters() {
        return new String[] { "className" };
    }

    @Override
    public void run(String commandName) throws Exception {
        Class parsedClass = Class.forName(className);
        if(OptionsHolder.class.isAssignableFrom(parsedClass)) {
            OptionsExtractor extractor = contextProvider.get().getInjector().getInstance(OptionsExtractor.class);
            OptionHolderDescription description = extractor.describeAllOptions(parsedClass);
            KrowTable table = extractor.getDescriptionTable(description);

            String asciiTable = table.print();
            String htmlTable = table.print(new HtmlTableFormatter());
            htmlTable = htmlTable.replaceAll("<table>", "<table class=\"table\">");

            Clog.i("\n{}", asciiTable);

            if (server != null && server.getWebsocket() != null) {
                server.getWebsocket().sendMessage("describe", htmlTable);
            }
        }
    }
}

