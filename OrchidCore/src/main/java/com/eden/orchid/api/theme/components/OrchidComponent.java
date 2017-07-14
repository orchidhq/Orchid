package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Data;

import javax.inject.Inject;

@Data
public abstract class OrchidComponent extends Prioritized implements OptionsHolder {

    protected String key;
    protected OrchidContext context;
    protected OrchidResources resources;
    protected OrchidPage page;

    private boolean rendered;

    @Option
    protected String[] templates;

    @Inject
    public OrchidComponent(int priority, String key, OrchidContext context, OrchidResources resources) {
        setPriority(priority);
        this.key = key;
        this.context = context;
        this.resources = resources;
    }

    public void prepare(OrchidPage page) {
        this.page = page;
    }
}
