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

    fun getSnippet(name: String): Snippet? {
        return snippets
            .singleOrNull { it.name == name }
            ?: run { printSnippetNotFoundWarning(name, emptyList()); null }
    }

    fun getSnippet(name: String, tags: List<String>): Snippet? {
        return snippets
            .singleOrNull { it.name == name && it.tags.containsAll(tags) }
            ?: run { printSnippetNotFoundWarning(name, tags); null }
    }

    fun getSnippets(tags: List<String>): List<Snippet> {
        return snippets
            .filter { it.tags.containsAll(tags) }
            .also {
                if(it.isEmpty()) {
                    printSnippetNotFoundWarning(null, tags)
                }
            }
    }

    private fun printSnippetNotFoundWarning(name: String?, tags: List<String>) {
        var message = "Snippet"

        if(name != null) {
            message += " named [$name]"
        }
        if(!tags.isEmpty()) {
            message += " with tags [${tags.joinToString()}]"
        }
        message += " not found"

        Clog.e(message)
    }
}
