package com.eden.orchid.impl.themes.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.flexible
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.utilities.SuppressedWarnings

@Description(value = "Locate and display your project's Readme file.", name = "Readme")
@Suppress(SuppressedWarnings.DEPRECATION)
class ReadmeComponent : OrchidComponent("readme") {
    val content: String?
        get() = context
            .getFlexibleResourceSource(LocalResourceSource, null)
            .findClosestFile(context, "readme")
            ?.compileContent(context, this)
}
