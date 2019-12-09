package com.eden.orchid.wiki.adapter

import com.eden.orchid.api.theme.components.ModularType
import com.eden.orchid.wiki.model.WikiSection
import com.eden.orchid.wiki.pages.WikiPage
import com.eden.orchid.wiki.pages.WikiSummaryPage

interface WikiAdapter : ModularType {

    fun loadWikiPages(section: WikiSection) : Pair<WikiSummaryPage, List<WikiPage>>?

}
