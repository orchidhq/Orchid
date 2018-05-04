package com.eden.orchid.swiftdoc.swift.statements

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.swiftdoc.swift.SwiftAttributes
import com.eden.orchid.swiftdoc.swift.SwiftMember
import com.eden.orchid.swiftdoc.swift.SwiftMembers
import com.eden.orchid.swiftdoc.swift.SwiftStatement
import org.json.JSONObject

class SwiftGlobal(context: OrchidContext, data: JSONObject, resource: OrchidResource)
    : SwiftStatement(context, data, "global", resource),
        SwiftAttributes,
        SwiftMembers {

    var extends: String? = null

    override lateinit var attributes: List<String>
    override lateinit var members: List<SwiftMember>

    override fun init() {
        super.init()

        extends = (data.optJSONArray("key.inheritedtypes")?.first() as? JSONObject)?.optString("key.name")

        initAttributes(data)
        initMembers(data)
    }

}