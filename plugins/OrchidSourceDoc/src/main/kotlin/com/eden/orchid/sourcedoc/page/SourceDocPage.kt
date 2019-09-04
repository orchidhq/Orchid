package com.eden.orchid.sourcedoc.page

import com.copperleaf.kodiak.common.AutoDocument
import com.copperleaf.kodiak.common.CommentComponent.Companion.TYPE_NAME
import com.copperleaf.kodiak.common.DocElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.sourcedoc.SourcedocGenerator
import com.eden.orchid.utilities.OrchidUtils

class SourceDocPage<T : DocElement>(
    val sourcedocGenerator: SourcedocGenerator<*>,
    context: OrchidContext,
    val module: String,
    val element: T,
    key: String,
    title: String
) : OrchidPage(
    SourceDocResource(
        context,
        if(module.isNotBlank()) "${sourcedocGenerator.key}/$module" else sourcedocGenerator.key,
        element
    ),
    key,
    title
) {

    override val itemIds = listOf(element.id, element.name)

    override fun getTemplates(): List<String> {
        return listOf(
            "${sourcedocGenerator.key.decapitalize()}${element.kind.capitalize()}",
            "sourceDocPage"
        )
    }

    override fun loadAssets() {
        addCss("assets/css/orchidSourceDoc.scss")
    }

    fun getRootSection(): Section {
        return Section(null, title, listOf(element))
    }

    fun getSectionsData(element: DocElement): List<Section> {
        if (element is AutoDocument) {
            return element
                .nodes
                .filter { it.getter().isNotEmpty() }
                .map { Section(element, it.prop.name, it.getter()) }
        }

        return emptyList()
    }

    fun renderSignature(element: DocElement): String {
        return element
            .signature
            .map {
                if (it.kind == TYPE_NAME) {
                    context.linkToPage(it.text, "", "", it.value, "")
                } else {
                    it.text
                }
            }
            .joinToString("")
    }

    fun renderComment(element: DocElement): String {
        return element
            .comment
            .components
            .map {
                if (it.kind == TYPE_NAME) {
                    context.linkToPage(it.text, sourcedocGenerator.key, "", it.value, "")
                } else {
                    it.text
                }
            }
            .joinToString("")
    }

    fun sectionId(section: Section): String {
        return if(section.parent != null && section.parent !== element) {
            OrchidUtils.sha1("${unhashedElementId(section.parent)}_${section.name}")
        }
        else {
            section.name
        }
    }

    fun elementId(element: DocElement): String {
        return OrchidUtils.sha1(unhashedElementId(element))
    }

    private fun unhashedElementId(element: DocElement): String {
        return element.signature.joinToString("_") { it.text }
    }

    data class Section(
        val parent: DocElement?,
        val name: String,
        val elements: List<DocElement>
    )
}
