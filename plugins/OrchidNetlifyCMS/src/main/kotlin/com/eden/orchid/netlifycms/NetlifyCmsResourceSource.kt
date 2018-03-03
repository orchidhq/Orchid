package com.eden.orchid.netlifycms

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetlifyCmsResourceSource @Inject
constructor(context: OrchidContext) : PluginResourceSource(context, 20)

