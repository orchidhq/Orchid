package com.eden.orchid.impl.themes.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.components.OrchidComponent;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public final class PageContentComponent extends OrchidComponent {

    @Getter @Setter @Option @BooleanDefault(true) protected boolean noWrapper;

    @Inject
    public PageContentComponent(OrchidContext context) {
        super(context, "pageContent", 100);
    }

}
