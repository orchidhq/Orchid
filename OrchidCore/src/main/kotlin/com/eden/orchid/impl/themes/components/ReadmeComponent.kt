package com.eden.orchid.impl.themes.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.components.OrchidComponent
import javax.inject.Inject

@Description(value = "Locate and display your project's Readme file.", name = "Readme")
class ReadmeComponent
@Inject
constructor(
        context: OrchidContext
) : OrchidComponent(context, "readme", 50) {
    val content: String? get() = context.findClosestFile("readme")?.compileContent(this)
}
