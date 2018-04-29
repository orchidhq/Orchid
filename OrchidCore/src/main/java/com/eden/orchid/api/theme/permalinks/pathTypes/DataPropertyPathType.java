package com.eden.orchid.api.theme.permalinks.pathTypes;

import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.permalinks.PermalinkPathType;

import javax.inject.Inject;

public final class DataPropertyPathType extends PermalinkPathType {

    private StringConverter converter;

    @Inject
    public DataPropertyPathType(StringConverter converter) {
        super(1);
        this.converter = converter;
    }

    @Override
    public boolean acceptsKey(OrchidPage page, String key) {
        Object o = page.getAllData().query(key);
        return o != null;
    }

    @Override
    public String format(OrchidPage page, String key) {
        Object o = page.getAllData().query(key);
        return converter.convert(o).second;
    }

}
