package com.eden.orchid.api.resources.resource

import com.eden.common.json.JSONElement
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.asInputStream
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

/**
 * A Resource type that provides a JSON data as content to a template. When used with renderTemplate() or renderString(),
 * this resource will supply additional data to the template renderer. When used with renderRaw(), the raw JSON-encoded
 * data will be written directly instead.
 */
class JsonResource
private constructor(
    reference: OrchidReference,
    private val hardcodedJson: JSONElement
) : OrchidResource(reference) {

    constructor(reference: OrchidReference, jsonList: JSONArray)        : this(reference, JSONElement(jsonList))
    constructor(reference: OrchidReference, jsonList: List<Any?>)       : this(reference, JSONElement(JSONArray(jsonList)))
    constructor(reference: OrchidReference, jsonMap: JSONObject)        : this(reference, JSONElement(jsonMap))
    constructor(reference: OrchidReference, jsonMap: Map<String, Any?>) : this(reference, JSONElement(JSONObject(jsonMap)))

    override fun getContentStream(): InputStream {
        return if (hardcodedJson.element is JSONObject) {
            (hardcodedJson.element as JSONObject).toString(2).asInputStream()
        } else if (hardcodedJson.element is JSONArray) {
            (hardcodedJson.element as JSONArray).toString(2).asInputStream()
        } else {
            hardcodedJson.toString().asInputStream()
        }
    }

}
