package com.eden.orchid.kss

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource

import javax.inject.Inject

class KssResourceSource @Inject
constructor(context: OrchidContext) : PluginResourceSource(context, 20)

