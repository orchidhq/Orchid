package com.eden.orchid.impl.resources

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resourceSource.JarResourceSource
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.google.inject.Provider

import javax.inject.Inject

class CoreResourceSource
@Inject
constructor(
        context: Provider<OrchidContext>
) : JarResourceSource(context, 1), PluginResourceSource
