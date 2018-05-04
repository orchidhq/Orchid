package com.eden.orchid.swiftdoc.swift.members

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.swiftdoc.swift.SwiftAttributes
import com.eden.orchid.swiftdoc.swift.SwiftMember
import com.eden.orchid.swiftdoc.swift.SwiftStatement
import org.json.JSONObject

class SwiftClassVar(context: OrchidContext, data: JSONObject, origin: SwiftStatement)
    : SwiftMember(context, data, "classVar", origin),
        SwiftAttributes {

    override lateinit var attributes: List<String>

    var name: String? = null
    var type: String? = null

    override fun init() {
        super.init()
        initAttributes(data)

        name = data.optString("key.name")
        type = data.optString("key.typename")
    }

}