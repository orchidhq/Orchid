package com.eden.orchid.snippets

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.snippets.adapter.EmbeddedSnippetsAdapter
import com.eden.orchid.snippets.adapter.FileSnippetsAdapter
import com.eden.orchid.snippets.adapter.RemoteSnippetsAdapter
import com.eden.orchid.snippets.adapter.SnippetsAdapter
import com.eden.orchid.snippets.components.SnippetComponent
import com.eden.orchid.snippets.components.SnippetsComponent
import com.eden.orchid.snippets.functions.SnippetFunction
import com.eden.orchid.snippets.tags.SnippetTag
import com.eden.orchid.snippets.tags.SnippetsTag
import com.eden.orchid.utilities.addToSet

class SnippetsModule : OrchidModule() {
    override fun configure() {
        withResources()

        addToSet<OrchidGenerator<*>, SnippetsGenerator>()
        addToSet<SnippetsAdapter>(
            FileSnippetsAdapter::class,
            EmbeddedSnippetsAdapter::class,
            RemoteSnippetsAdapter::class)

        addToSet<TemplateFunction, SnippetFunction>()
        addToSet<TemplateTag>(
            SnippetTag::class,
            SnippetsTag::class)
        addToSet<OrchidComponent>(
            SnippetComponent::class,
            SnippetsComponent::class)
    }
}

