package com.eden.orchid.swiftdoc.page

import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.swiftdoc.swift.SwiftStatement

class SwiftdocSourcePage(resource: OrchidResource, val statements: List<SwiftStatement>, val codeJson: String)
    : OrchidPage(resource, "swiftdocSource") {



}
