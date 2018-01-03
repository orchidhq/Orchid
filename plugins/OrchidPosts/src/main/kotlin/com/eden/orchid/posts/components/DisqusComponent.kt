package com.eden.orchid.posts.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent

import javax.inject.Inject

class DisqusComponent @Inject
constructor(context: OrchidContext) : OrchidComponent(context, "disqus", 100) {

    @Option
    lateinit var shortname: String

    @Option
    lateinit var identifier: String

}

