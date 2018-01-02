package com.eden.orchid.wiki.model

import com.eden.orchid.wiki.pages.WikiPage
import com.eden.orchid.wiki.pages.WikiSummaryPage

data class WikiSection(
        val section: String?,
        val summaryPage: WikiSummaryPage,
        val wikiPages: List<WikiPage>
)

