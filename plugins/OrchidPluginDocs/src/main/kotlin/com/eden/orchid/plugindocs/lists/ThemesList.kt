package com.eden.orchid.plugindocs.lists

import com.eden.orchid.api.server.admin.AdminList
import com.eden.orchid.api.theme.Theme
import java.util.TreeSet
import javax.inject.Inject

@JvmSuppressWildcards
class ThemesList @Inject
constructor(list: Set<Theme>) : AdminList {

    private val list: Set<Theme>

    init {
        this.list = TreeSet(list)
    }

    override fun getKey(): String {
        return "themes"
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

