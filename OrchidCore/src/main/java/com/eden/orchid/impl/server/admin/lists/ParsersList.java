package com.eden.orchid.impl.server.admin.lists;

import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.utilities.ObservableTreeSet;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

public class ParsersList implements AdminList<OrchidParser> {

    private Set<OrchidParser> list;

    @Inject
    public ParsersList(Set<OrchidParser> list) {
        this.list = new ObservableTreeSet<>(list);
    }

    @Override
    public String getKey() {
        return "parsers";
    }

    @Override
    public Collection<OrchidParser> getItems() {
        return list;
    }

    @Override
    public String getItemId(OrchidParser item) {
        return item.getClass().getSimpleName();
    }

    @Override
    public OrchidParser getItem(String id) {
        for(OrchidParser item : getItems()) {
            if(id.equals(item.getClass().getSimpleName())) {
                return item;
            }
        }
        return null;
    }
}
