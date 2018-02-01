package com.eden.orchid.pluginDocs.lists

import com.eden.orchid.api.compilers.OrchidCompiler
import com.eden.orchid.api.server.admin.AdminList
import java.util.TreeSet
import javax.inject.Inject

@JvmSuppressWildcards
class CompilersList @Inject
constructor(list: Set<OrchidCompiler>) : AdminList {

    private val list: Set<OrchidCompiler>

    init {
        this.list = TreeSet(list)
    }

    override fun getKey(): String {
        return "compilers"
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

