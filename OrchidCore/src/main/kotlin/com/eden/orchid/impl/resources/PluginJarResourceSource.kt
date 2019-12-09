package com.eden.orchid.impl.resources

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourcesource.JarResourceSource
import com.eden.orchid.api.resources.resourcesource.PluginResourceSource
import javax.inject.Inject

class PluginJarResourceSource
@Inject
constructor(
    moduleClass: Class<out OrchidModule>,
    priority: Int
) : JarResourceSource(moduleClass, priority), PluginResourceSource
