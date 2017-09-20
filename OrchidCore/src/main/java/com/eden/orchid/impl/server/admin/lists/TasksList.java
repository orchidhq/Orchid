package com.eden.orchid.impl.server.admin.lists;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.tasks.OrchidTask;
import com.google.inject.Provider;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class TasksList implements AdminList<OrchidTask> {

    private Set<OrchidTask> list;
    private Provider<OrchidContext> contextProvider;

    @Inject
    public TasksList(Provider<OrchidContext> contextProvider) {
        this.contextProvider = contextProvider;
    }

    @Override
    public String getKey() {
        return "tasks";
    }

    @Override
    public Collection<OrchidTask> getItems() {
        if(list == null) {
            list = new TreeSet<>(contextProvider.get().resolveSet(OrchidTask.class));
        }
        return list;
    }

    @Override
    public String getItemId(OrchidTask item) {
        return item.getClass().getSimpleName();
    }

    @Override
    public OrchidTask getItem(String id) {
        for(OrchidTask item : getItems()) {
            if(id.equals(item.getClass().getSimpleName())) {
                return item;
            }
        }
        return null;
    }
}
