package com.eden.orchid.posts.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.posts.FeedsGenerator
import com.eden.orchid.posts.model.FeedsModel
import com.eden.orchid.utilities.resolve

@Description("Add links to the page <head> with links to the post feeds.", name = "Feed Links")
class FeedLinksComponent : OrchidComponent("feedLinks", true) {

    val model: FeedsModel by lazy {
        context.resolve<FeedsModel>()
    }

    val feeds: List<FeedsGenerator.FeedPage> get() = model.feeds
}
