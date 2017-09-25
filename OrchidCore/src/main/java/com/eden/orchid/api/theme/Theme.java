package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.theme.menus.OrchidMenu;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public abstract class Theme extends AbstractTheme {

    @Getter @Setter @Option protected OrchidMenu menu;

    @Inject
    public Theme(OrchidContext context, String key, int priority) {
        super(context, key, priority);
    }

}
