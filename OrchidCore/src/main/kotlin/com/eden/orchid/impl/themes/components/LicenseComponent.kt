package com.eden.orchid.impl.themes.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.utilities.SuppressedWarnings

@Description(value = "Locate and display your project's License file.", name = "License")
@Suppress(SuppressedWarnings.DEPRECATION)
class LicenseComponent : OrchidComponent("license") {
    val content: String?
        get() = context
            .getFlexibleResourceSource(LocalResourceSource, null)
            .findClosestFile(context, "license")
            ?.compileContent(context, this)
}
