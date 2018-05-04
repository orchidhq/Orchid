package com.eden.orchid.swiftdoc

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import javax.inject.Inject

class SwiftdocResourceSource @Inject
constructor(context: OrchidContext) : PluginResourceSource(context, 10)
