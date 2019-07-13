package com.eden.orchid.impl.themes.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.components.OrchidComponent

@Description(value = "Locate and display your project's Readme file.", name = "Readme")
class ReadmeComponent : OrchidComponent("readme", 50) {
    val content: String? get() = context.findClosestFile("readme")?.compileContent(this)
}
