package com.eden.orchid.impl.tasks;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.tasks.OrchidCommand;
import com.google.inject.Provider;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

@Description("Publish the Orchid build results.")
public final class DeployCommand extends OrchidCommand {

    private final Provider<OrchidContext> contextProvider;

    @Getter @Setter
    @Option @BooleanDefault(true)
    @Description("Whether to run a dry deploy, validating all options but not actually deploying anything.")
    private boolean dry;

    @Inject
    public DeployCommand(Provider<OrchidContext> contextProvider) {
        super(100, "deploy");
        this.contextProvider = contextProvider;
    }

    @Override
    public String[] parameters() {
        return new String[] {"dry"};
    }

    @Override
    public void run(String commandName) throws Exception {
        contextProvider.get().deploy(dry);
    }
}

