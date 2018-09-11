package com.eden.orchid.testhelpers

import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resource.JsonResource
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resourceSource.LocalResourceSource
import com.eden.orchid.api.resources.resourceSource.OrchidResourceSource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.google.inject.Provider
import org.json.JSONObject
import java.util.ArrayList
import javax.inject.Inject

class TestConfigResourceSource
@Inject
constructor(
        private val context: Provider<OrchidContext>,
        private val mockConfig: Map<String, Any>
) : OrchidResourceSource, LocalResourceSource {

    override val priority: Int
        get() = Integer.MAX_VALUE - 1

    override fun getResourceEntry(fileName: String): OrchidResource? {
        if (fileName == "config.yml") {
            val el = JSONElement(JSONObject(mockConfig))
            val ref = OrchidReference(context.get(), "config.yml")

            return JsonResource(el, ref)
        }

        return null
    }

    override fun getResourceEntries(dirName: String, fileExtensions: Array<String>?, recursive: Boolean): List<OrchidResource> {
        return ArrayList()
    }

    fun toModule(): OrchidModule {
        return object : OrchidModule() {
            override fun configure() {
                addToSet(LocalResourceSource::class.java, this@TestConfigResourceSource)
            }
        }
    }


}
