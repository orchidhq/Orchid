package com.eden.orchid.impl.publication;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.publication.OrchidPublisher;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public class ScriptPublisher extends OrchidPublisher {

    @Getter @Setter
    @Option
    @Description("The executable name")
    private String command;

    @Getter @Setter
    @Option
    @Description("Args to pass to the executable")
    private String[] args;

    @Inject
    public ScriptPublisher(OrchidContext context) {
        super(context, "script", 100);
    }

    @Override
    public boolean validate() {
        return !EdenUtils.isEmpty(command);
    }

    @Override
    public void publish() {
        Clog.v("> [{}] [{}]", command, String.join(" ", args));
    }

}
