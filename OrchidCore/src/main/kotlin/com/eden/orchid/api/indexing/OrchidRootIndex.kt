package com.eden.orchid.api.indexing

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.impl.generators.ExternalIndexGenerator
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.SuppressedWarnings
import javax.inject.Singleton

@Singleton
class OrchidRootIndex(val context: OrchidContext, ownKey: String) : OrchidIndex(null, ownKey) {
    companion object {
        const val queryPagesErrorMessage = "This method has been removed. Grouping these pages by a Taxonomy instead."
    }

    val allIndexedPages = LinkedHashMap<String, Pair<OrchidIndex, OrchidGenerator.Model>>()

    fun addChildIndex(key: String, index: OrchidIndex, model: OrchidGenerator.Model) {
        allIndexedPages[key] = index to model

        if(key != ExternalIndexGenerator.GENERATOR_KEY) {
            for (page in index.allPages) {
                this.addToIndex(page.reference.path, page)
            }
        }
    }

    @Suppress(SuppressedWarnings.UNCHECKED_KOTLIN)
    fun <T : OrchidGenerator.Model> getChildIndex(generator: String): T? = allIndexedPages[generator]?.second as? T

    fun getChildIndices(generators: Array<String>): List<OrchidGenerator.Model> =
        generators.mapNotNull { getChildIndex<OrchidGenerator.Model>(it) }

    override val allPages: List<OrchidPage>
        get() = allIndexedPages.values.flatMap { it.first.allPages }

    override fun addToIndex(taxonomy: String, page: OrchidPage) = super.addToIndex("$ownKey/$taxonomy", page)

    override fun find(taxonomy: String): List<OrchidPage> = super.find("$ownKey/$taxonomy")

    override fun findPage(taxonomy: String): OrchidPage? = super.findPage("$ownKey/$taxonomy")

    override fun findIndex(taxonomy: String): OrchidIndex? = super.findIndex("$ownKey/$taxonomy")

    fun find(taxonomy: String, childKey: String): List<OrchidPage> =
        allIndexedPages[childKey]?.first?.find("$childKey/$taxonomy") ?: emptyList()

    override fun toString(): String {
        return "root " + super.toString()
    }

// Helper Methods
//----------------------------------------------------------------------------------------------------------------------

    fun findChildPages(page: OrchidPage): List<OrchidPage> {
        val index = findIndex(page.reference.path)

        return if (index != null) {
            index.children.values.flatMap { it.getOwnPages() }
        } else {
            emptyList()
        }
    }

    fun findChildPages(
        collectionType: String?,
        collectionId: String?,
        itemId: String?,
        defaultPage: OrchidPage
    ): List<OrchidPage> {
        return findChildPages(defaultPage.context.findPageOrDefault(collectionType, collectionId, itemId, defaultPage))
    }

    fun findSiblingPages(page: OrchidPage): List<OrchidPage> {
        val pagePath = page.reference.path.split("/").dropLast(1).joinToString("/")
        val index = findIndex(pagePath)

        return if (index != null) {
            index.children.values.flatMap { it.getOwnPages() }
        } else {
            emptyList()
        }
    }

    fun findSiblingPages(
        collectionType: String?,
        collectionId: String?,
        itemId: String?,
        defaultPage: OrchidPage
    ): List<OrchidPage> {
        return findSiblingPages(
            defaultPage.context.findPageOrDefault(
                collectionType,
                collectionId,
                itemId,
                defaultPage
            )
        )
    }

    @Deprecated(queryPagesErrorMessage)
    fun queryPages(itemId: String, collectionType: String?, collectionId: String?): List<OrchidPage> {
        Clog.e(queryPagesErrorMessage)
        return emptyList()
    }

    fun findPageByServerPath(path: String): OrchidPage? {
        val requestedPath = OrchidUtils.normalizePath(path)

        return allIndexedPages
            .values
            .stream()
            .flatMap { it.first.allPages.stream() }
            .filter {  page ->
                val outputPath = OrchidUtils.normalizePath(page.reference.path)
                val outputName = if (EdenUtils.isEmpty(OrchidUtils.normalizePath(page.reference.outputExtension))) {
                    OrchidUtils.normalizePath(page.reference.fileName)
                } else {
                    OrchidUtils.normalizePath(page.reference.fileName) + "." + OrchidUtils.normalizePath(
                        page.reference.outputExtension
                    )
                }

                val pagePath = OrchidUtils.normalizePath(outputPath + "/" + outputName)

                pagePath == requestedPath
            }
            .findFirst()
            .orElse(null)
    }

}
