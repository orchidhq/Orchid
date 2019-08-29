package com.eden.orchid.sourcedoc.menu

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.menus.MenuItem
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.sourcedoc.model.SourceDocModel
import com.eden.orchid.sourcedoc.page.SourceDocPage

@Description("Locate all source pages of a given kind.", name = "Source Pages")
class SourceDocPagesMenuItemType : OrchidMenuFactory("sourcedocPages") {

    @Option
    lateinit var title: String

    @Option
    lateinit var module: String

    @Option
    lateinit var node: String

    override fun getMenuItems(
        context: OrchidContext,
        page: OrchidPage
    ): List<MenuItem> {
        return try {
            val model: SourceDocModel? =
                context.resolve(SourceDocModel::class.java, module)
                ?: context.resolve(SourceDocModel::class.java)

            if (model != null ) {
                if (node.isNotBlank()) {
                    val pages: List<SourceDocPage<*>> = model
                        .nodes
                        .entries
                        .firstOrNull { it.key.prop.name == node }
                        ?.value
                        ?: emptyList()

                    pages
                        .sortedBy { it.title }
                        .map {
                            MenuItem.Builder(context)
                                .page(it)
                                .build()
                        }
                } else {
                    model
                        .nodes
                        .map { node ->
                            val nodeTitle = "All ${node.key.prop.name.capitalize()}"
                            val nodePages = node.value.sortedBy { it.title }

                            MenuItem.Builder(context)
                                .title(nodeTitle)
                                .pages(nodePages)
                                .build()
                        }
                }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}
