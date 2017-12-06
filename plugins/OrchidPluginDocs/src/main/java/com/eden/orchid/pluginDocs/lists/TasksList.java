package com.eden.orchid.pluginDocs.lists;

import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.api.tasks.OrchidTask;
import com.google.inject.Provider;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public final class TasksList implements AdminList<OrchidTask> {

    private final Provider<Set<OrchidTask>> tasksSetProvider;

    @Inject
    public TasksList(Provider<Set<OrchidTask>> tasksSetProvider) {
        this.tasksSetProvider = tasksSetProvider;
    }

    @Override
    public String getKey() {
        return "tasks";
    }

    @Override
    public Collection<OrchidTask> getItems() {
        return new TreeSet<>(tasksSetProvider.get());
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
