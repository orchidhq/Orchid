package com.eden.orchid.posts.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent

import javax.inject.Inject

@Description("Easily add comments to any page with Disqus.", name = "Disqus")
class DisqusComponent : OrchidComponent("disqus", 100) {

    @Option
    @Description("Your disqus shortname.")
    lateinit var shortname: String

    @Option
    @Description("A site-wide unique identifier for the comment section on this page. Defaults to the page's URL.")
    lateinit var identifier: String

}

