package com.eden.orchid.sourcedoc.menu

import com.caseyjbrooks.clog.Clog
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
    lateinit var moduleType: String

    @Option
    lateinit var moduleName: String

    @Option
    lateinit var node: String

    override fun getMenuItems(
        context: OrchidContext,
        page: OrchidPage
    ): List<MenuItem> {
        return try {
            val model: SourceDocModel? =
                context.resolve(SourceDocModel::class.java, moduleType) ?: context.resolve(SourceDocModel::class.java)

            // no model, return early
            if (model == null) return emptyList()

            val module = if (model.modules.size > 1 && moduleName.isNotBlank()) {
                model.modules.firstOrNull { it.name == moduleName }
            } else if (model.modules.size == 1) {
                model.modules.single()
            } else {
                Clog.e("Cannot find module of type '{}' named '{}'", moduleType, moduleName)
                null
            }

            if (module == null) return emptyList()
            if (node.isNotBlank()) {
                val pages: List<SourceDocPage<*>> = module
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
                module
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

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}
