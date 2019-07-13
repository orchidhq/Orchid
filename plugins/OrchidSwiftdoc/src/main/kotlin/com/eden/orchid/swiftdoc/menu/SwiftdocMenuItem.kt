package com.eden.orchid.swiftdoc.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.swiftdoc.SwiftdocModel
import com.eden.orchid.swiftdoc.page.SwiftdocStatementPage

@Description("Links to all Swift elements of a particular type.", name = "Swift Pages")
class SwiftdocMenuItem : OrchidMenuFactory("swiftdocPages") {

    @Option
    lateinit var docType: String

    @Option
    lateinit var title: String

    override fun getMenuItems(context: OrchidContext): List<MenuItem> {
        val model = context.resolve(SwiftdocModel::class.java)

        val pages: List<SwiftdocStatementPage> = when (docType) {
            "class" -> model.classPages
            "struct" -> model.structPages
            "enum" -> model.enumPages
            "protocol" -> model.protocolPages
            "global" -> model.globalPages
            else -> emptyList()
        }

        if (pages.isNotEmpty()) {
            return listOf(
                MenuItem.Builder(context)
                    .title(title)
                    .pages(pages.sortedBy { it.statement.name })
                    .build()
            )
        }

        return emptyList()
    }

}