package com.eden.orchid.impl.server.admin.lists;

import com.eden.orchid.api.options.OrchidFlag;
import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.api.server.admin.AdminList;

import javax.inject.Inject;
import java.util.Collection;

public final class OptionsList implements AdminList<OrchidFlag> {

    private final Collection<OrchidFlag> list;

    @Inject
    public OptionsList() {
        list = OrchidFlags.getInstance().getFlags();
    }

    @Override
    public String getKey() {
        return "options";
    }

    @Override
    public Collection<OrchidFlag> getItems() {
        return list;
    }

    @Override
    public String getItemId(OrchidFlag item) {
        return item.getClass().getSimpleName();
    }

    @Override
    public OrchidFlag getItem(String id) {
        for(OrchidFlag item : getItems()) {
            if(id.equals(item.getClass().getSimpleName())) {
                return item;
            }
        }
        return null;
    }
}
