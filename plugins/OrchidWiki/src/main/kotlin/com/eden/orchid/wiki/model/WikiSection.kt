package com.eden.orchid.wiki.model

import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.wiki.WikiGenerator
import com.eden.orchid.wiki.pages.WikiPage
import com.eden.orchid.wiki.pages.WikiSummaryPage
import javax.inject.Inject

@Archetypes(
        Archetype(value = ConfigArchetype::class, key = "${WikiGenerator.GENERATOR_KEY}.defaultConfig")
)
class WikiSection
@Inject
constructor() : OptionsHolder {

    @Option
    var key: String? = null

    @Option
    @BooleanDefault(true)
    @Description("If true, the title of each page in the wiki will be prepended with its numerical order in the wiki.")
    var includeIndexInPageTitle: Boolean = true

    lateinit var summaryPage: WikiSummaryPage
    lateinit var wikiPages: List<WikiPage>

}

