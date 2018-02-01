package com.eden.orchid.plugindocs.lists

import com.eden.orchid.api.resources.resourceSource.FileResourceSource
import com.eden.orchid.api.resources.resourceSource.OrchidResourceSource
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.server.admin.AdminList
import java.util.TreeSet
import javax.inject.Inject

@JvmSuppressWildcards
class ResourceSourcesList @Inject
constructor(fileResourceSources: Set<FileResourceSource>, pluginResourceSources: Set<PluginResourceSource>) : AdminList {

    private val list: TreeSet<OrchidResourceSource>

    init {
        list = TreeSet()
        list.addAll(fileResourceSources)
        list.addAll(pluginResourceSources)
    }

    override fun getKey(): String {
        return "resourceSources"
    }

    override fun getItems(): Collection<Any> {
        return list
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

