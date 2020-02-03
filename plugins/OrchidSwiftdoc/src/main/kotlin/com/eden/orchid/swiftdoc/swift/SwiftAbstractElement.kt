package com.eden.orchid.swiftdoc.swift

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.readToString
import org.json.JSONObject

abstract class SwiftAbstractElement(
        val context: OrchidContext,
        val data: JSONObject,
        val resource: OrchidResource
) {

    lateinit var comment: String
    var offset: Int = 0
    var length: Int = 0
    var nameoffset: Int = 0
    var namelength: Int = 0

    val origin: OrchidReference = resource.reference

    open fun init() {
        comment = data.optString("key.doc.comment")
        offset = data.optInt("key.offset")
        length = data.optInt("key.length")
        nameoffset = data.optInt("key.nameoffset")
        namelength = data.optInt("key.namelength")
    }

    fun debug(): String {
        return data.toString(2)
    }

    fun comments(): String {
        return context.compile("md", comment)
    }

    fun text(): String {
        return try {
            resource.contentStream.readToString()?.substring(offset - 1, offset + length) ?: ""
        }
        catch (e: Exception) {
            ""
        }
    }

    fun nameText(): String {
        return try {
            resource.contentStream.readToString()?.substring(nameoffset - 1, nameoffset + namelength) ?: ""
        }
        catch (e: Exception) {
            ""
        }
    }

}
