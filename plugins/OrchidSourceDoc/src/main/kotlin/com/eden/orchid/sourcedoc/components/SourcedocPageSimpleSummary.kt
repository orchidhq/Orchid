package com.eden.orchid.sourcedoc.components

import com.copperleaf.kodiak.common.RichTextComponent
import com.copperleaf.kodiak.common.RichTextComponent.Companion.INHERITED
import com.copperleaf.kodiak.common.RichTextComponent.Companion.TYPE_NAME
import com.copperleaf.kodiak.common.TopLevel
import com.eden.orchid.api.theme.components.ComponentHolder
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.sourcedoc.page.SourceDocPage

class SourcedocPageSimpleSummary : OrchidComponent("sourceDocSummary") {

    override fun canBeUsedOnPage(
        containingPage: OrchidPage?,
        componentHolder: ComponentHolder?,
        possibleComponents: MutableList<MutableMap<String, Any>>?,
        currentComponents: MutableList<OrchidComponent>?
    ): Boolean {
        return containingPage is SourceDocPage<*>
    }

    private val containingPage: SourceDocPage<*> by lazy { page as SourceDocPage<*> }

    private val topLevelNode: TopLevel? by lazy {
        if (containingPage.element is TopLevel) containingPage.element as TopLevel else null
    }

    val pageParentHierarchy: List<RichTextComponent> by lazy {
        val immediateParents = topLevelNode
            ?.parents
            ?.singleOrNull { it.kind == INHERITED }

        // TODO("calculate full inheritance hierarchy")

        (immediateParents?.let { listOf(it) } ?: emptyList()) +
                RichTextComponent(TYPE_NAME, containingPage.element.name, containingPage.element.id)
    }

    val pageContexts: List<RichTextComponent> by lazy {
        topLevelNode
            ?.contexts
            ?: emptyList()
    }

    val pageDirectChildren: List<RichTextComponent> by lazy {
        // TODO("calculate direct children")
        emptyList<RichTextComponent>()
    }

    val pageIndirectChildren: List<RichTextComponent> by lazy {
//        TODO("calculate indirect children")
        emptyList<RichTextComponent>()
    }

}
