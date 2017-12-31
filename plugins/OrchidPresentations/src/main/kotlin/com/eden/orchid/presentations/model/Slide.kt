package com.eden.orchid.presentations.model

import com.eden.orchid.api.resources.resource.OrchidResource

class Slide(val slideContent: OrchidResource) {

    val content: String
        get() {
            return slideContent.compileContent(null)
        }
}
