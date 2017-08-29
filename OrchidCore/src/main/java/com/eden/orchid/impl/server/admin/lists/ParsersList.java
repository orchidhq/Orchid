package com.eden.orchid.impl.server.admin.lists;

import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.server.admin.AdminList;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class ParsersList implements AdminList<OrchidParser> {

    private Set<OrchidParser> list;

    @Inject
    public ParsersList(Set<OrchidParser> list) {
        this.list = new TreeSet<>(list);
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
