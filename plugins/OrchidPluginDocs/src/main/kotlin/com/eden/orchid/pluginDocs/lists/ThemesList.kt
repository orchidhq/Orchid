package com.eden.orchid.pluginDocs.lists

import com.eden.orchid.api.server.admin.AdminList
import com.eden.orchid.api.theme.Theme
import java.util.*
import javax.inject.Inject

@JvmSuppressWildcards
class ThemesList @Inject
constructor(list: Set<Theme>) : AdminList<Theme> {

    private val list: Set<Theme>

    init {
        this.list = TreeSet(list)
    }

    override fun getKey(): String {
        return "themes"
    }

    override fun getItems(): Collection<Theme> {
        return list
    }

    override fun getItemId(item: Theme): String {
        return item.javaClass.simpleName
    }

    override fun getItem(id: String): Theme? {
        for (item in items) {
            if (id == item.javaClass.simpleName) {
                return item
            }
        }
        return null
    }
}

