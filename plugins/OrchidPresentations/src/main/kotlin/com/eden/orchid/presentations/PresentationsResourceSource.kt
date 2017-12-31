package com.eden.orchid.presentations

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource

import javax.inject.Inject

class PresentationsResourceSource @Inject
constructor(context: OrchidContext) : PluginResourceSource(context, 20)
