package com.eden.orchid.pluginDocs.lists;

import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.theme.Theme;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public final class ThemesList implements AdminList<Theme> {

    private final Set<Theme> list;

    @Inject
    public ThemesList(Set<Theme> list) {
        this.list = new TreeSet<>(list);
    }

    @Override
    public String getKey() {
        return "themes";
    }

    @Override
    public Collection<Theme> getItems() {
        return list;
    }

    @Override
    public String getItemId(Theme item) {
        return item.getClass().getSimpleName();
    }

    @Override
    public Theme getItem(String id) {
        for(Theme item : getItems()) {
            if(id.equals(item.getClass().getSimpleName())) {
                return item;
            }
        }
        return null;
    }
}
