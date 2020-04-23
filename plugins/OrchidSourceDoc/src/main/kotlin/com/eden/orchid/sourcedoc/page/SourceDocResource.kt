package com.eden.orchid.sourcedoc.page

import com.copperleaf.kodiak.common.AutoDocument
import com.copperleaf.kodiak.common.DocElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.sourcedoc.menu.SourceDocPageLinksMenuItemType
import com.eden.orchid.utilities.asInputStream
import com.eden.orchid.utilities.readToString
import java.io.InputStream

class SourceDocResource<T : DocElement>(
    context: OrchidContext,
    val element: T
) : OrchidResource(OrchidReference(context,"")) {

    init {
        reference.extension = "md"
    }

    override fun getContentStream(): InputStream {
        var ownCommentText = element.getCommentText()

        val pageSections = getSectionsData(element)
        for (section in pageSections) {
            section
                .elements
                .map { sectionElement ->
                    ownCommentText += "${sectionElement.name}\n"
                    ownCommentText += "${sectionElement.getCommentText()}\n"
                }
        }

        return ownCommentText.asInputStream()
    }

    private fun DocElement.getCommentText() : String = comment
        .components
        .joinToString(" ") { it.text }
        .asInputStream()
        .readToString() ?: ""

    fun getSectionsData(element: DocElement): List<SourceDocPage.Section> {
        if (element is AutoDocument) {
            return element
                .nodes
                .filter { it.elements.isNotEmpty() }
                .map { SourceDocPage.Section(element, it.name, it.elements) }
        }

        return emptyList()
    }
}
