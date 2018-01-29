package com.eden.orchid.api.theme.permalinks;

import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.theme.pages.OrchidPage;

public abstract class PermalinkPathType extends Prioritized {

    public PermalinkPathType(int priority) {
        super(priority);
    }

    public abstract boolean acceptsKey(OrchidPage page, String key);

    public abstract String format(OrchidPage page, String key);

}
