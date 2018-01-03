package com.eden.orchid.pluginDocs.lists

import com.eden.orchid.api.server.admin.AdminList
import com.eden.orchid.api.theme.components.OrchidComponent
import java.util.*
import javax.inject.Inject

@JvmSuppressWildcards
class ComponentsList @Inject
constructor(list: Set<OrchidComponent>) : AdminList<OrchidComponent> {

    private val list: Set<OrchidComponent>

    init {
        this.list = TreeSet(list)
    }

    override fun getKey(): String {
        return "components"
    }

    override fun getItems(): Collection<OrchidComponent> {
        return list
    }

    override fun getItemId(item: OrchidComponent): String {
        return item.javaClass.simpleName
    }

    override fun getItem(id: String): OrchidComponent? {
        for (item in items) {
            if (id == item.javaClass.simpleName) {
                return item
            }
        }
        return null
    }
}

