package com.eden.orchid.plugindocs.lists

import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.server.admin.AdminList

import javax.inject.Inject

@JvmSuppressWildcards
class OptionsList @Inject
constructor() : AdminList {

    private val list: Collection<OrchidFlag>

    init {
        list = OrchidFlags.getInstance().flags
    }

    override fun getKey(): String {
        return "options"
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

