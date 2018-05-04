package com.eden.orchid.swiftdoc.swift.members

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.swiftdoc.swift.SwiftAttributes
import com.eden.orchid.swiftdoc.swift.SwiftMember
import com.eden.orchid.swiftdoc.swift.SwiftStatement
import org.json.JSONObject

class SwiftClassMethod(context: OrchidContext, data: JSONObject, origin: SwiftStatement)
    : SwiftMember(context, data, "classMethod", origin),
        SwiftAttributes {

    override lateinit var attributes: List<String>

    var name: String? = null

    override fun init() {
        super.init()
        initAttributes(data)

        name = data.optString("key.name")
    }

}