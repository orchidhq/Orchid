package com.eden.orchid.swiftdoc.page

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.swiftdoc.SwiftdocGenerator
import com.eden.orchid.swiftdoc.swift.SwiftStatement

@Description(value = "A page describing an element in your Swift code.", name = "Swift Statement")
@Archetype(value = ConfigArchetype::class, key = "${SwiftdocGenerator.GENERATOR_KEY}.pages")
class SwiftdocStatementPage(resource: BaseSwiftdocResource, val statement: SwiftStatement)
    : OrchidPage(resource, "swiftdoc" + statement.kind.capitalize(), statement.name) {

    override val itemIds: List<String>
        get() = listOf(statement.name ?: "")

    fun debug(): String {
        return statement.debug()
    }

}
