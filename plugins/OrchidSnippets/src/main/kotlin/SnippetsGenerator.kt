package com.eden.orchid.snippets

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.snippets.models.Snippet
import com.eden.orchid.snippets.models.SnippetsModel

class SnippetsGenerator : OrchidGenerator<SnippetsModel>("snippets", Stage.CONTENT) {
    override fun startIndexing(context: OrchidContext): SnippetsModel {
        return sequence<Snippet> {
                addFileSnippets(context.getResourceEntries("snippets", null, false, null))
            }
            .toList()
            .let { SnippetsModel(it) }
    }

    private suspend fun SequenceScope<Snippet>.addFileSnippets(
        resources: List<OrchidResource>
    ) {
        resources.forEach {
            yield(
                Snippet(
                    it.reference.originalFileName,
                    listOf("tag_${it.reference.originalFileName}"),
                    it,
                    0,
                    null
                )
            )
        }
    }
}
