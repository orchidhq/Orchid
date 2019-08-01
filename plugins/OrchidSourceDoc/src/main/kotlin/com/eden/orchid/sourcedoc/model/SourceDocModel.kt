package com.eden.orchid.sourcedoc.model

import com.copperleaf.kodiak.common.AutoDocumentNode
import com.copperleaf.kodiak.common.ModuleDoc
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.sourcedoc.page.SourceDocPage

class SourceDocModel(
    val moduleDoc: ModuleDoc?,
    val nodes: Map<AutoDocumentNode, List<SourceDocPage<*>>>
) : OrchidGenerator.Model {

    override val allPages by lazy {
        nodes.values.flatten()
    }

}