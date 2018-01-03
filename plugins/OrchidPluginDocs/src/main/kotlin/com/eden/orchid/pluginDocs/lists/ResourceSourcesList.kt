package com.eden.orchid.pluginDocs.lists

import com.eden.orchid.api.resources.resourceSource.FileResourceSource
import com.eden.orchid.api.resources.resourceSource.OrchidResourceSource
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.server.admin.AdminList

import javax.inject.Inject
import java.util.TreeSet

@JvmSuppressWildcards
class ResourceSourcesList @Inject
constructor(fileResourceSources: Set<FileResourceSource>, pluginResourceSources: Set<PluginResourceSource>) : AdminList<OrchidResourceSource> {

    private val list: TreeSet<OrchidResourceSource>

    init {
        list = TreeSet()
        list.addAll(fileResourceSources)
        list.addAll(pluginResourceSources)
    }

    override fun getKey(): String {
        return "resourceSources"
    }

    override fun getItems(): Collection<OrchidResourceSource> {
        return list
    }

    override fun getItemId(item: OrchidResourceSource): String {
        return item.javaClass.simpleName
    }

    override fun getItem(id: String): OrchidResourceSource? {
        for (item in items) {
            if (id == item.javaClass.simpleName) {
                return item
            }
        }
        return null
    }
}

