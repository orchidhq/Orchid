package com.eden.orchid.pluginDocs.lists;

import com.eden.orchid.api.resources.resourceSource.FileResourceSource;
import com.eden.orchid.api.resources.resourceSource.OrchidResourceSource;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.server.admin.AdminList;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public final class ResourceSourcesList implements AdminList<OrchidResourceSource> {

    private final TreeSet<OrchidResourceSource> list;

    @Inject
    public ResourceSourcesList(Set<FileResourceSource> fileResourceSources, Set<PluginResourceSource> pluginResourceSources) {
        list = new TreeSet<>();
        list.addAll(fileResourceSources);
        list.addAll(pluginResourceSources);
    }

    @Override
    public String getKey() {
        return "resourceSources";
    }

    @Override
    public Collection<OrchidResourceSource> getItems() {
        return list;
    }

    @Override
    public String getItemId(OrchidResourceSource item) {
        return item.getClass().getSimpleName();
    }

    @Override
    public OrchidResourceSource getItem(String id) {
        for(OrchidResourceSource item : getItems()) {
            if(id.equals(item.getClass().getSimpleName())) {
                return item;
            }
        }
        return null;
    }
}
