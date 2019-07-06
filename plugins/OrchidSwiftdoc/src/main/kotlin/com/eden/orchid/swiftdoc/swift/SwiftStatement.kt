package com.eden.orchid.swiftdoc.swift

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.swiftdoc.SwiftdocModel
import com.eden.orchid.swiftdoc.swift.statements.SwiftExtension
import com.eden.orchid.swiftdoc.swift.statements.SwiftTypealias
import org.json.JSONObject

abstract class SwiftStatement(
        context: OrchidContext,
        data: JSONObject,
        val kind: String,
        resource: OrchidResource
) : SwiftAbstractElement(context, data, resource) {

    var name: String? = null

    override fun init() {
        super.init()
        name = data.optString("key.name")
    }

    val extensions: List<SwiftExtension>
        get() {
            return context.resolve(SwiftdocModel::class.java).extensionsFor(this)
        }

    val aliases: List<SwiftTypealias>
        get() {
            return context.resolve(SwiftdocModel::class.java).aliasesFor(this)
        }

}