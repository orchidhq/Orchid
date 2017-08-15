package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

@Getter @Setter
public abstract class OrchidComponent extends Prioritized implements OptionsHolder {

    protected String key;
    protected OrchidContext context;
    protected OrchidPage page;

    private boolean rendered;

    @Option
    protected String[] templates;

    @Inject
    public OrchidComponent(int priority, String key, OrchidContext context) {
        setPriority(priority);
        this.key = key;
        this.context = context;
    }

    public void prepare(OrchidPage page) {
        this.page = page;
    }
}
