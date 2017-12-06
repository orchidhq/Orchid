package com.eden.orchid.pluginDocs.lists;

import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public final class ComponentsList implements AdminList<OrchidComponent> {

    private Set<OrchidComponent> list;

    @Inject
    public ComponentsList(Set<OrchidComponent> list) {
        this.list = new TreeSet<>(list);
    }

    @Override
    public String getKey() {
        return "components";
    }

    @Override
    public Collection<OrchidComponent> getItems() {
        return list;
    }

    @Override
    public String getItemId(OrchidComponent item) {
        return item.getClass().getSimpleName();
    }

    @Override
    public OrchidComponent getItem(String id) {
        for(OrchidComponent item : getItems()) {
            if(id.equals(item.getClass().getSimpleName())) {
                return item;
            }
        }
        return null;
    }
}
