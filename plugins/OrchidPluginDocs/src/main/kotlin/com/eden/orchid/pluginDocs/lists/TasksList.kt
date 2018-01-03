package com.eden.orchid.pluginDocs.lists

import com.eden.orchid.api.server.admin.AdminList
import com.eden.orchid.api.tasks.OrchidTask
import com.google.inject.Provider
import java.util.*
import javax.inject.Inject

@JvmSuppressWildcards
class TasksList @Inject
constructor(private val tasksSetProvider: Provider<Set<OrchidTask>>) : AdminList<OrchidTask> {

    override fun getKey(): String {
        return "tasks"
    }

    override fun getItems(): Collection<OrchidTask> {
        return TreeSet(tasksSetProvider.get())
    }

    override fun getItemId(item: OrchidTask): String {
        return item.javaClass.simpleName
    }

    override fun getItem(id: String): OrchidTask? {
        for (item in items) {
            if (id == item.javaClass.simpleName) {
                return item
            }
        }
        return null
    }
}

