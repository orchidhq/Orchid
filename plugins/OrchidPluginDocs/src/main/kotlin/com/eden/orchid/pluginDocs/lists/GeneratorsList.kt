package com.eden.orchid.pluginDocs.lists

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.server.admin.AdminList
import java.util.TreeSet
import javax.inject.Inject

@JvmSuppressWildcards
class GeneratorsList @Inject
constructor(list: Set<OrchidGenerator>) : AdminList {

    private val list: Set<OrchidGenerator>

    init {
        this.list = TreeSet(list)
    }

    override fun getKey(): String {
        return "generators"
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

