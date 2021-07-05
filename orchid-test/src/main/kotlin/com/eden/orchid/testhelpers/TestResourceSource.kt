package com.eden.orchid.testhelpers

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resourcesource.HardcodedResourceSource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource

class TestResourceSource(
    mockResources: List<(OrchidContext) -> OrchidResource>
) : OrchidResourceSource by HardcodedResourceSource(
    mockResources,
    Integer.MAX_VALUE,
    LocalResourceSource
) {
    fun toModule(): OrchidModule {
        return TestResourceSourceModule(this)
    }
}

private class TestResourceSourceModule(private val resourceSource: TestResourceSource) : OrchidModule() {
    override fun configure() {
        addToSet(OrchidResourceSource::class.java, resourceSource)
    }
}
