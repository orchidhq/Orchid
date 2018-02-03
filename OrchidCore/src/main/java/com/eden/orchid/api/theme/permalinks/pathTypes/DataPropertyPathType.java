package com.eden.orchid.api.theme.permalinks.pathTypes;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.permalinks.PermalinkPathType;

import javax.inject.Inject;

public final class DataPropertyPathType extends PermalinkPathType {

    @Inject
    public DataPropertyPathType() {
        super(1);
    }

    @Override
    public boolean acceptsKey(OrchidPage page, String key) {
        return page.getData().has(key) && page.getData().get(key) != null;
    }

    @Override
    public String format(OrchidPage page, String key) {
        return page.getData().get(key).toString();
    }

}
