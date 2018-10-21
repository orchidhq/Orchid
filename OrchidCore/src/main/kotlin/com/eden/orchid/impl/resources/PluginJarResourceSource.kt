package com.eden.orchid.impl.resources

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.JarResourceSource
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.google.inject.Provider
import javax.inject.Inject

class PluginJarResourceSource
@Inject
constructor(
        context: Provider<OrchidContext>,
        moduleClass: Class<out OrchidModule>,
        priority: Int
) : JarResourceSource(context, moduleClass, priority), PluginResourceSource
