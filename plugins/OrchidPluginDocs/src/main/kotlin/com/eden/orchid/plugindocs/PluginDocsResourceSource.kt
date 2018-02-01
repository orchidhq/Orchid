package com.eden.orchid.plugindocs

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource

import javax.inject.Inject

class PluginDocsResourceSource @Inject
constructor(context: OrchidContext) : PluginResourceSource(context, 1000)
