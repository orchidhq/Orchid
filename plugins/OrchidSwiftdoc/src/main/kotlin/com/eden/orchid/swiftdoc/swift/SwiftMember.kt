package com.eden.orchid.swiftdoc.swift

import com.eden.orchid.api.OrchidContext
import org.json.JSONObject

abstract class SwiftMember(
        context: OrchidContext,
        data: JSONObject,
        val kind: String,
        val statement: SwiftStatement
) : SwiftAbstractElement(context, data, statement.resource) {

}