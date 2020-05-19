package com.eden.orchid.sourcedoc.page

import com.copperleaf.kodiak.common.AutoDocument
import com.copperleaf.kodiak.common.RichTextComponent.Companion.TYPE_NAME
import com.copperleaf.kodiak.common.DocElement
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.assets.AssetManagerDelegate
import com.eden.orchid.api.theme.components.ComponentHolder
import com.eden.orchid.sourcedoc.functions.SourcedocAnchorFunction
import com.eden.orchid.utilities.OrchidUtils

class SourceDocPage<T : DocElement>(
    resource: SourceDocResource<*>,
    val element: T,
    key: String,
    title: String,
    moduleType: String,
    moduleGroup: String,
    module: String,
    moduleSlug: String
) : BaseSourceDocPage(
    resource,
    key,
    title,
    moduleType,
    moduleGroup,
    module,
    moduleSlug
) {

    override val itemIds = listOf(element.id, element.name)

    @Option
    @Description(
        "Components to inject into SourceDocPages to provide additional info or context about the element."
    )
    lateinit var summaryComponents: ComponentHolder

    override fun getComponentHolders(): Array<ComponentHolder> {
        return super.getComponentHolders() + summaryComponents
    }

    override fun getTemplates(): List<String> {
        return listOf(
            "${generator.key.decapitalize()}${element.kind.capitalize()}",
            "sourceDocPage"
        )
    }

    override fun loadAssets(delegate: AssetManagerDelegate) {
        delegate.addCss("assets/css/orchidSourceDoc.scss")
    }

    fun getRootSection(): Section {
        return Section(null, title, listOf(element))
    }

    fun getSectionsData(element: DocElement): List<Section> {
        if (element is AutoDocument) {
            return element
                .nodes
                .filter { it.elements.isNotEmpty() }
                .map { Section(element, it.name, it.elements) }
        }

        return emptyList()
    }

    fun renderComment(element: DocElement): String {
        return element
            .comment
            .components
            .map {
                if (it.kind == TYPE_NAME) {
                    SourcedocAnchorFunction.getLinkToSourcedocPage(context, this, it.text, it.value ?: "")
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
        val elements: List<DocElement>,
        val hasDescendants: Boolean = elements.any { it is AutoDocument && it.nodes.any { node -> node.elements.isNotEmpty() } }
    )
}
