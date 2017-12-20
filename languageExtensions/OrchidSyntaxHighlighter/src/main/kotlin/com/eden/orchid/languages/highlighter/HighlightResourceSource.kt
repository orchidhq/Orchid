package com.eden.orchid.languages.highlighter

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource

import javax.inject.Inject

class HighlightResourceSource @Inject
constructor(context: OrchidContext) : PluginResourceSource(context, 750)