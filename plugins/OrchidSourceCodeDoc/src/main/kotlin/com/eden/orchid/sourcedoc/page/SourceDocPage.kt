package com.eden.orchid.sourcedoc.page

import com.copperleaf.kodiak.common.AutoDocument
import com.copperleaf.kodiak.common.DocElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.sourcedoc.SourcedocGenerator

class SourceDocPage<T : DocElement>(
    val sourcedocGenerator: SourcedocGenerator<*>,
    context: OrchidContext,
    val element: T,
    key: String,
    title: String
) : OrchidPage(SourceDocResource(context, element), key, title) {

    override val itemIds = listOf(element.id, element.name)

    override fun getTemplates(): List<String> {
        return listOf(
            "${sourcedocGenerator.key.decapitalize()}${element.kind.capitalize()}",
            "sourceDocPage"
        )
    }

    override fun loadAssets() {
        addCss("assets/css/orchidSourcedoc.scss")
    }

    fun getRootSection(): Section {
        return Section(title, listOf(element))
    }

    fun getSectionsData(element: DocElement): List<Section> {
        if (element is AutoDocument) {
            return element
                .nodes
                .filter { it.getter().isNotEmpty() }
                .map { Section(it.prop.name, it.getter()) }
        }

        return emptyList()
    }

    data class Section(
        val name: String,
        val elements: List<DocElement>
    )
}
