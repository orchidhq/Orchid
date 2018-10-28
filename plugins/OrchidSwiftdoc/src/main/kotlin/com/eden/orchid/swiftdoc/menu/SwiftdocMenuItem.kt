package com.eden.orchid.swiftdoc.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemImpl
import com.eden.orchid.swiftdoc.SwiftdocModel
import com.eden.orchid.swiftdoc.page.SwiftdocStatementPage
import javax.inject.Inject

@Description("Links to all Swift elements of a particular type.", name = "Swift Pages")
class SwiftdocMenuItem
@Inject
constructor(
        context: OrchidContext,
        val model: SwiftdocModel
) : OrchidMenuItem(context, "swiftdocPages", 100) {

    @Option
    lateinit var docType: String

    @Option
    lateinit var title: String

    override fun getMenuItems(): List<OrchidMenuItemImpl> {
        val pages: List<SwiftdocStatementPage> = when(docType) {
            "class"    -> model.classPages
            "struct"   -> model.structPages
            "enum"     -> model.enumPages
            "protocol" -> model.protocolPages
            "global"   -> model.globalPages
            else -> emptyList()
        }

        if(!pages.isEmpty()) {
            return listOf(
                    OrchidMenuItemImpl.Builder(context)
                            .title(title)
                            .pages(pages.sortedBy { it.statement.name })
                            .build()
            )
        }

        return emptyList()
    }

}