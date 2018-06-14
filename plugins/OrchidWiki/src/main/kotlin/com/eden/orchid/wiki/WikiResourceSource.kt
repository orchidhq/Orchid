package com.eden.orchid.wiki

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WikiResourceSource @Inject
constructor(context: OrchidContext) : PluginResourceSource(context, 50)

