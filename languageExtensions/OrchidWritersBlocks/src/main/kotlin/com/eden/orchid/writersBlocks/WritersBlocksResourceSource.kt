package com.eden.orchid.writersBlocks

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WritersBlocksResourceSource @Inject
constructor(context: OrchidContext) : PluginResourceSource(context, 20)

