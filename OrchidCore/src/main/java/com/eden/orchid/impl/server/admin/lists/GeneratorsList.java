package com.eden.orchid.impl.server.admin.lists;

import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.server.admin.AdminList;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public final class GeneratorsList implements AdminList<OrchidGenerator> {

    private Set<OrchidGenerator> list;

    @Inject
    public GeneratorsList(Set<OrchidGenerator> list) {
        this.list = new TreeSet<>(list);
    }

    @Override
    public String getKey() {
        return "generators";
    }

    @Override
    public Collection<OrchidGenerator> getItems() {
        return list;
    }

    @Override
    public String getItemId(OrchidGenerator item) {
        return item.getClass().getSimpleName();
    }

    @Override
    public OrchidGenerator getItem(String id) {
        for(OrchidGenerator item : getItems()) {
            if(id.equals(item.getClass().getSimpleName())) {
                return item;
            }
        }
        return null;
    }
}
