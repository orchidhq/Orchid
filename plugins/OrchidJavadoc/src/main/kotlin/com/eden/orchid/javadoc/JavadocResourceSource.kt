package com.eden.orchid.javadoc

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import javax.inject.Inject

class JavadocResourceSource @Inject
constructor(context: OrchidContext) : PluginResourceSource(context, 10)
