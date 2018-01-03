package com.eden.orchid.pluginDocs.lists

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.server.admin.AdminList
import java.util.*
import javax.inject.Inject

@JvmSuppressWildcards
class GeneratorsList @Inject
constructor(list: Set<OrchidGenerator>) : AdminList<OrchidGenerator> {

    private val list: Set<OrchidGenerator>

    init {
        this.list = TreeSet(list)
    }

    override fun getKey(): String {
        return "generators"
    }

    override fun getItems(): Collection<OrchidGenerator> {
        return list
    }

    override fun getItemId(item: OrchidGenerator): String {
        return item.javaClass.simpleName
    }

    override fun getItem(id: String): OrchidGenerator? {
        for (item in items) {
            if (id == item.javaClass.simpleName) {
                return item
            }
        }
        return null
    }
}

