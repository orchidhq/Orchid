package com.eden.orchid.testhelpers

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resource.JsonResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource
import com.eden.orchid.api.theme.pages.OrchidReference
import java.util.ArrayList
import javax.inject.Inject

class TestConfigResourceSource
@Inject
constructor(
    private val mockConfig: Map<String, Any>
) : OrchidResourceSource {

    override val priority: Int
        get() = Integer.MAX_VALUE

    override val scope: OrchidResourceSource.Scope = LocalResourceSource

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        if (fileName == "config.yml") {
            return JsonResource(
                OrchidReference(context, "config.yml"),
                mockConfig
            )
        }

        return null
    }

    override fun getResourceEntries(
        context: OrchidContext,
        dirName: String,
        fileExtensions: Array<String>?,
        recursive: Boolean
    ): List<OrchidResource> {
        return ArrayList()
    }

    fun toModule(): OrchidModule {
        return TestConfigResourceSourceModule(this)
    }
}

private class TestConfigResourceSourceModule(private val resourceSource: TestConfigResourceSource) : OrchidModule() {
    override fun configure() {
        addToSet(OrchidResourceSource::class.java, resourceSource)
    }
}
