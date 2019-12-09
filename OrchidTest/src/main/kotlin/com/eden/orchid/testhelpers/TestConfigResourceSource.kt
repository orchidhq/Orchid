package com.eden.orchid.testhelpers

import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resource.JsonResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource
import com.eden.orchid.api.theme.pages.OrchidReference
import org.json.JSONObject
import java.util.ArrayList
import javax.inject.Inject

class TestConfigResourceSource
@Inject
constructor(
    private val mockConfig: Map<String, Any>
) : OrchidResourceSource, LocalResourceSource {

    override val priority: Int
        get() = Integer.MAX_VALUE - 1

    override fun getResourceEntry(context: OrchidContext, fileName: String): OrchidResource? {
        if (fileName == "config.yml") {
            val el = JSONElement(JSONObject(mockConfig))
            val ref = OrchidReference(context, "config.yml")

            return JsonResource(el, ref)
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
        addToSet(LocalResourceSource::class.java, resourceSource)
    }
}
