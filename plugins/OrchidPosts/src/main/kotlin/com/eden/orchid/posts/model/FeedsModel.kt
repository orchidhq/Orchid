package com.eden.orchid.posts.model

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.FeedsGenerator
import com.eden.orchid.posts.pages.AuthorPage
import com.eden.orchid.posts.pages.PostPage

class FeedsModel
constructor(
    val context: OrchidContext,
    val feeds: List<FeedsGenerator.FeedPage>
) : OrchidGenerator.Model {
    override val allPages: List<OrchidPage> = feeds
}

