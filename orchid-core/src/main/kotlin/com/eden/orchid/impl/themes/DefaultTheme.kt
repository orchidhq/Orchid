package com.eden.orchid.impl.themes

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.Theme

import javax.inject.Inject

@Description(
    value = "The default theme, which adds no assets and lets you build your theme entirely by custom templates.",
    name = "Default"
)
class DefaultTheme @Inject
constructor(context: OrchidContext) : Theme(context, "Default")
