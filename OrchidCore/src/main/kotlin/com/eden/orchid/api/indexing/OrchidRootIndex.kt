package com.eden.orchid.api.indexing

import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.HashMap
import javax.inject.Singleton

@Singleton
class OrchidRootIndex(ownKey: String) : OrchidIndex(null, ownKey) {
    val allIndexedPages = HashMap<String, OrchidIndex>()

    fun addChildIndex(key: String, index: OrchidIndex) {
        allIndexedPages[key] = index
        for (page in index.allPages) {
            this.addToIndex(page.reference.path, page)
        }
    }

    fun getChildIndex(generator: String): List<OrchidPage> = allIndexedPages[generator]?.allPages ?: emptyList()

    fun getChildIndices(generators: Array<String>): List<OrchidPage> = generators.flatMap { it -> getChildIndex(it) }

    override val allPages: List<OrchidPage>
        get() = allIndexedPages.values.flatMap { it -> it.allPages }

    override fun addToIndex(taxonomy: String, page: OrchidPage) = super.addToIndex("$ownKey/$taxonomy", page)

    override fun find(taxonomy: String): List<OrchidPage> = super.find("$ownKey/$taxonomy")

    override fun findPage(taxonomy: String): OrchidPage? = super.findPage("$ownKey/$taxonomy")

    override fun findIndex(taxonomy: String): OrchidIndex? = super.findIndex("$ownKey/$taxonomy")

    fun find(taxonomy: String, childKey: String): List<OrchidPage> = allIndexedPages[childKey]?.find("$childKey/$taxonomy") ?: emptyList()

    override fun toString(): String {
        return "root " + super.toString()
    }
}
