package com.eden.orchid.forms

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchResourceSource @Inject
constructor(context: OrchidContext) : PluginResourceSource(context, 20)
