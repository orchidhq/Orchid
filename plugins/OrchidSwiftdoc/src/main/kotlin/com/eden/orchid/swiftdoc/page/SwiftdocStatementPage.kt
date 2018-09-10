package com.eden.orchid.swiftdoc.page

import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.swiftdoc.swift.SwiftStatement

class SwiftdocStatementPage(resource: BaseSwiftdocResource, val statement: SwiftStatement)
    : OrchidPage(resource, "swiftdoc" + statement.kind.capitalize(), statement.name) {

    fun debug(): String {
        return statement.debug()
    }

}
