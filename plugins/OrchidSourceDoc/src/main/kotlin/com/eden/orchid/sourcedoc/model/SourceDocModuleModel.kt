package com.eden.orchid.sourcedoc.model

import com.copperleaf.kodiak.common.AutoDocumentNode
import com.copperleaf.kodiak.common.ModuleDoc
import com.eden.orchid.sourcedoc.page.SourceDocModuleHomePage
import com.eden.orchid.sourcedoc.page.SourceDocPage

class SourceDocModuleModel(
    val homepage: SourceDocModuleHomePage,
    val name: String,
    val moduleDoc: ModuleDoc?,
    val nodes: Map<AutoDocumentNode, List<SourceDocPage<*>>>
) {

    val allPages by lazy {
        listOf(homepage) + nodes.values.flatten()
    }

}
