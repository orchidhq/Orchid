package com.eden.orchid.snippets.models

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage

class SnippetsModel(
    val snippets: List<Snippet>
) : OrchidGenerator.Model {
    override val allPages: List<OrchidPage> = emptyList()
    override val collections: List<OrchidCollection<*>> = emptyList()

    val tags: List<String> = snippets.flatMap { it.tags }.distinct()

    fun getSnippet(query: SnippetQuery): Snippet? {
        return snippets
            .singleOrNull { it.name == query.snippetName }
            ?: run { printSnippetNotFoundWarning(query, null); null }
    }

    fun getSnippets(query: SnippetsQuery): List<Snippet> {
        return snippets
            .filter { it.tags.containsAll(query.snippetTags) }
            .also {
                if(it.isEmpty()) {
                    printSnippetNotFoundWarning(null, query)
                }
            }
    }

    private fun printSnippetNotFoundWarning(singleQuery: SnippetQuery?, tabsQuery: SnippetsQuery?) {
        var message = "Snippet"

        if(singleQuery != null) {
            message += " named [${singleQuery.snippetName}]"
        }
        if(tabsQuery != null) {
            message += " with tags [${tabsQuery.snippetTags.joinToString()}]"
        }
        message += " not found"

        Clog.e(message)
    }

    interface SnippetQuery {
        val snippetName: String
    }

    interface SnippetsQuery {
        var id: String
        var snippetTags: List<String>
    }
}
