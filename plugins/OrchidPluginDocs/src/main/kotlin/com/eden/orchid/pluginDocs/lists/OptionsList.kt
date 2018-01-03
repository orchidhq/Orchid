package com.eden.orchid.pluginDocs.lists

import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.server.admin.AdminList

import javax.inject.Inject

@JvmSuppressWildcards
class OptionsList @Inject
constructor() : AdminList<OrchidFlag> {

    private val list: Collection<OrchidFlag>

    init {
        list = OrchidFlags.getInstance().flags
    }

    override fun getKey(): String {
        return "options"
    }

    override fun getItems(): Collection<OrchidFlag> {
        return list
    }

    override fun getItemId(item: OrchidFlag): String {
        return item.javaClass.simpleName
    }

    override fun getItem(id: String): OrchidFlag? {
        for (item in items) {
            if (id == item.javaClass.simpleName) {
                return item
            }
        }
        return null
    }
}

