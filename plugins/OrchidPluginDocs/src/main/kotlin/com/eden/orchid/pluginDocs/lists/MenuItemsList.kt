package com.eden.orchid.pluginDocs.lists

import com.eden.orchid.api.server.admin.AdminList
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import java.util.*
import javax.inject.Inject

@JvmSuppressWildcards
class MenuItemsList @Inject
constructor(list: Set<OrchidMenuItem>) : AdminList<OrchidMenuItem> {

    private val list: Set<OrchidMenuItem>

    init {
        this.list = TreeSet(list)
    }

    override fun getKey(): String {
        return "menuItems"
    }

    override fun getItems(): Collection<OrchidMenuItem> {
        return list
    }

    override fun getItemId(item: OrchidMenuItem): String {
        return item.javaClass.simpleName
    }

    override fun getItem(id: String): OrchidMenuItem? {
        for (item in items) {
            if (id == item.javaClass.simpleName) {
                return item
            }
        }
        return null
    }
}

