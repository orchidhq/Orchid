package com.eden.orchid.plugindocs.lists

import com.eden.orchid.api.server.admin.AdminList
import com.eden.orchid.api.tasks.OrchidTask
import com.google.inject.Provider
import java.util.TreeSet
import javax.inject.Inject

@JvmSuppressWildcards
class TasksList @Inject
constructor(private val tasksSetProvider: Provider<Set<OrchidTask>>) : AdminList {

    override fun getKey(): String {
        return "tasks"
    }

    override fun getItems(): Collection<Any> {
        return TreeSet(tasksSetProvider.get())
    }

    override fun getItemId(item: Any): String {
        return item.javaClass.simpleName
    }

    override fun getItem(id: String): Any? {
        for (item in items) {
            if (id == item.javaClass.simpleName) {
                return item
            }
        }
        return null
    }
}

