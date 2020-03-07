package com.eden.orchid.snippets

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.snippets.models.SnippetsModel
import com.eden.orchid.snippets.models.SnippetsSection

class SnippetsGenerator : OrchidGenerator<SnippetsModel>("snippets", Stage.CONTENT) {

    @Option
    @Description("The sections within the baseDir to make wikis out of.")
    lateinit var sections: MutableList<SnippetsSection>

    @Option
    @StringDefault("file")
    @Description("The configuration for the default snippets loader section.")
    lateinit var defaultConfig: SnippetsSection

    override fun startIndexing(context: OrchidContext): SnippetsModel {
        if (sections.isEmpty()) {
            sections.add(defaultConfig)
        }

        return sequence {
            sections.forEach { section ->
                yieldAll(section.addSnippets(context))
            }
        }
            .sortedBy { it.name }
            .toList()
            .let { SnippetsModel(it) }
    }
}
