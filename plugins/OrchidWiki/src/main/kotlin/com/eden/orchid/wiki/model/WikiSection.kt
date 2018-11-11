package com.eden.orchid.wiki.model

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to
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
    lateinit var key: String

    @Option
    lateinit var title: String

    @Option @BooleanDefault(true)
    var createPdf: Boolean = true

    @Option
    @BooleanDefault(false)
    @Description("If true, the title of each page in the wiki will be prepended with its numerical order in the wiki.")
    var includeIndexInPageTitle: Boolean = false

    lateinit var summaryPage: WikiSummaryPage
    lateinit var wikiPages: List<WikiPage>

    val sectionTitle: String
        get() {
            return if (!EdenUtils.isEmpty(title)) title else key from { camelCase() } to { titleCase() }
        }

}

