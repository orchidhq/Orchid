package com.eden.orchid.api.theme.menus.menuItem;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.registration.Prioritized;
import lombok.Getter;

import java.util.List;

public abstract class OrchidMenuItem extends Prioritized implements OptionsHolder {

    protected final OrchidContext context;

    @Getter protected final String key;

    public OrchidMenuItem(OrchidContext context, String key, int priority) {
        super(priority);
        this.key = key;
        this.context = context;
    }

    public abstract List<OrchidMenuItemImpl> getMenuItems();

}
