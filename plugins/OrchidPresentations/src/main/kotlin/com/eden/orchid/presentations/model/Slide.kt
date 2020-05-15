package com.eden.orchid.presentations.model

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource

class Slide(
    val context: OrchidContext,
    val slideContent: OrchidResource,
    order: Int
) {

    private val slideOrderRegex = "(\\d+)-(.*)"

    val order: Int
    val id: String

    init {
        val resourcePath = slideContent.reference.path
        val regex = slideOrderRegex.toRegex().matchEntire(resourcePath)
        if(regex != null) {
            this.order = Integer.parseInt(regex.groups[1]!!.value)
            this.id = regex.groups[2]!!.value
        }
        else {
            this.order = order
            this.id = slideContent.reference.originalFileName
        }
    }

    val content: String
        get() {
            return slideContent.compileContent(context, null)
        }
}

