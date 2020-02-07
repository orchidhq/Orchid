package com.eden.orchid.impl.resources.resourcesource

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourcesource.JarResourceSource
import com.eden.orchid.api.resources.resourcesource.JarResourceSource.Companion.jarForClass
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource
import com.eden.orchid.api.resources.resourcesource.PluginResourceSource
import javax.inject.Inject

class PluginJarResourceSource
@Inject
constructor(
    moduleClass: Class<out OrchidModule>,
    priority: Int
) : OrchidResourceSource by JarResourceSource(moduleClass, jarForClass(moduleClass), priority, PluginResourceSource)
