package com.eden.orchid.swiftdoc.page

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.swiftdoc.swift.SwiftStatement

@Description(value = "A page describing the elements in a Swift source file.", name = "Swift Source")
class SwiftdocSourcePage(
        resource: OrchidResource,
        val statements: List<SwiftStatement>,
        val codeJson: String
) : OrchidPage(resource, "swiftdocSource", null) {


}
