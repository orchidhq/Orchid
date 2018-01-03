package com.eden.orchid.pluginDocs.lists

import com.eden.orchid.api.compilers.OrchidParser
import com.eden.orchid.api.server.admin.AdminList
import java.util.*
import javax.inject.Inject

@JvmSuppressWildcards
class ParsersList @Inject
constructor(list: Set<OrchidParser>) : AdminList<OrchidParser> {

    private val list: Set<OrchidParser>

    init {
        this.list = TreeSet(list)
    }

    override fun getKey(): String {
        return "parsers"
    }

    override fun getItems(): Collection<OrchidParser> {
        return list
    }

    override fun getItemId(item: OrchidParser): String {
        return item.javaClass.simpleName
    }

    override fun getItem(id: String): OrchidParser? {
        for (item in items) {
            if (id == item.javaClass.simpleName) {
                return item
            }
        }
        return null
    }
}

