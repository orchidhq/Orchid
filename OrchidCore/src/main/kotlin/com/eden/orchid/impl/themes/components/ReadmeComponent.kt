package com.eden.orchid.impl.themes.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.flexible
import com.eden.orchid.api.theme.components.OrchidComponent

@Description(value = "Locate and display your project's Readme file.", name = "Readme")
class ReadmeComponent : OrchidComponent("readme") {
    val content: String?
        get() = context
            .getDefaultResourceSource(LocalResourceSource, null)
            .flexible()
            .findClosestFile(context, "readme")
            ?.compileContent(context, this)
}
