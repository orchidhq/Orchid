package com.eden.orchid.api.theme.permalinks;

import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.theme.pages.OrchidPage;

import static com.eden.orchid.utilities.OrchidUtils.DEFAULT_PRIORITY;

public abstract class PermalinkPathType extends Prioritized {

    public PermalinkPathType(int priority) {
        super(priority);
    }

    public PermalinkPathType() {
        super(DEFAULT_PRIORITY);
    }

    public abstract boolean acceptsKey(OrchidPage page, String key);

    public abstract String format(OrchidPage page, String key);

}
