package com.eden.orchid.impl.themes.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.components.OrchidComponent

@Description(value = "Locate and display your project's License file.", name = "License")
class LicenseComponent : OrchidComponent("license", 40) {
    val content: String? get() = context.findClosestFile("license")?.compileContent(this)
}
