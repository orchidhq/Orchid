package com.eden.orchid.swiftdoc.swift.members

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.swiftdoc.swift.SwiftAttributes
import com.eden.orchid.swiftdoc.swift.SwiftMember
import com.eden.orchid.swiftdoc.swift.SwiftStatement
import org.json.JSONObject

class SwiftEnumCase(context: OrchidContext, data: JSONObject, origin: SwiftStatement)
    : SwiftMember(context, data, "enumCase", origin),
        SwiftAttributes {

    override lateinit var attributes: List<String>
    lateinit var cases: List<String>

    override fun init() {
        super.init()
        initAttributes(data)

        cases = data.optJSONArray("key.substructure")
                ?.map { it as JSONObject }
                ?.map { it.optString("key.name") }
                ?.filterNotNull() ?: emptyList()
    }

}