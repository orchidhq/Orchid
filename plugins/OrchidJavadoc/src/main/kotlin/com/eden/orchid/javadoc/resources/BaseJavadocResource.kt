package com.eden.orchid.javadoc.resources

import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.FreeableResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.sun.javadoc.Doc
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

open class BaseJavadocResource(
        context: OrchidContext,
        qualifiedName: String,
        var doc: Doc
) : FreeableResource(OrchidReference(context, qualifiedName.replace("\\.".toRegex(), "/") + ".html")) {

    init {
        reference.extension = "md"
    }

    override fun loadContent() {
        if (rawContent == null) {
            rawContent = doc.inlineTags().map { it.text() }.joinToString(" ")
            content = rawContent

            val tagMap = HashMap<String, MutableList<String>>()
            for (tag in doc.tags()) {
                val key = tag.name().substring(1)
                if (!tagMap.containsKey(key)) {
                    tagMap[key] = ArrayList()
                }
                tagMap[key]!!.add(tag.text())
            }

            val data = JSONObject()
            for ((key, value) in tagMap) {
                if (value.size == 1) {
                    data.put(key, value[0])
                }
                else {
                    data.put(key, value)
                }
            }

            this.embeddedData = JSONElement(data)
        }
    }

}
