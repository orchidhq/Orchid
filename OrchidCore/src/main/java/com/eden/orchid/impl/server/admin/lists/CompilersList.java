package com.eden.orchid.impl.server.admin.lists;

import com.eden.orchid.api.compilers.OrchidCompiler;
import com.eden.orchid.api.server.admin.AdminList;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public final class CompilersList implements AdminList<OrchidCompiler> {

    private Set<OrchidCompiler> list;

    @Inject
    public CompilersList(Set<OrchidCompiler> list) {
        this.list = new TreeSet<>(list);
    }

    @Override
    public String getKey() {
        return "compilers";
    }

    @Override
    public Collection<OrchidCompiler> getItems() {
        return list;
    }

    @Override
    public String getItemId(OrchidCompiler item) {
        return item.getClass().getSimpleName();
    }

    @Override
    public OrchidCompiler getItem(String id) {
        for(OrchidCompiler item : getItems()) {
            if(id.equals(item.getClass().getSimpleName())) {
                return item;
            }
        }
        return null;
    }
}
