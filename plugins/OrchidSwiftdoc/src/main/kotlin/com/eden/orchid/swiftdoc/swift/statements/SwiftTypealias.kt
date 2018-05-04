package com.eden.orchid.swiftdoc.swift.statements

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.swiftdoc.swift.SwiftAttributes
import com.eden.orchid.swiftdoc.swift.SwiftMember
import com.eden.orchid.swiftdoc.swift.SwiftMembers
import com.eden.orchid.swiftdoc.swift.SwiftStatement
import org.json.JSONObject

class SwiftTypealias(context: OrchidContext, data: JSONObject, resource: OrchidResource)
    : SwiftStatement(context, data, "typealias", resource),
        SwiftAttributes,
        SwiftMembers {

    override lateinit var attributes: List<String>
    override lateinit var members: List<SwiftMember>

    lateinit var target: String

    override fun init() {
        super.init()
        initAttributes(data)
        initMembers(data)

        val aliasParts = text().split("=")
        target = if(aliasParts.isNotEmpty()) aliasParts.last().trim() else ""
    }

}