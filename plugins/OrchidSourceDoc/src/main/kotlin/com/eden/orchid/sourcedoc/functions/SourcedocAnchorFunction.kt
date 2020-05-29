package com.eden.orchid.sourcedoc.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.sourcedoc.model.SourceDocModel
import com.eden.orchid.sourcedoc.page.BaseSourceDocPage

class SourcedocAnchorFunction : TemplateFunction("sourceDocAnchor", true) {

    @Option
    @Description("The title to display in an anchor tag for the given item if found. Otherwise, the title is " + "returned directly.")
    lateinit var title: String

    @Option
    @Description("The Id of an item to link to.")
    lateinit var itemId: String

    override fun parameters(): Array<String> = arrayOf(::title.name, ::itemId.name)

    override fun apply(context: OrchidContext, page: OrchidPage?): Any {
        return when(page) {
            is BaseSourceDocPage -> getLinkToSourcedocPage(context, page, title, itemId) // use current page info to compute best match for the target
            else -> context.linkToPage(
                page,       // from
                    title,  // title
                    "",     // collectionType
                    "",     // collectionId
                    itemId, // itemId
                    "",     // customClasses
                    ""      // pageAnchorId
                ) // treat it as a normal page link (such as to external pages)
        }
    }

    companion object {
        fun getLinkToSourcedocPage(
            context: OrchidContext,
            sourcePage: BaseSourceDocPage,
            title: String,
            itemId: String
        ): String {
            return getLinkToSourcedocPage(context, sourcePage, sourcePage.moduleType, sourcePage.module, title, itemId)
        }

        fun getLinkToSourcedocPage(
            context: OrchidContext,
            sourcePage: OrchidPage,
            sourceModuleType: String,
            sourceModule: String,
            title: String,
            itemId: String
        ): String {
            val module = SourceDocModel.getModule(context, sourceModuleType, sourceModule)
            if(module == null) return ""

            val seq = if(sourceModule != sourceModuleType && module.relatedModules.isNotEmpty()) {
                // multi-module setup
                sequenceOf(
                    sourceModule,                         // link to own module first
                    *module.relatedModules.toTypedArray() // link to related modules next
                )
            }
            else {
                // single-module setup
                sequenceOf(sourceModule)
            }

            return seq
                .filter { it.isNotBlank() }
                .map { moduleName ->
                    context.linkToPage(
                        sourcePage,                                 // from
                        title,                                      // title
                        sourceModuleType,                           // collectionType
                        moduleName,                                 // collectionId
                        itemId.takeIf { it.isNotBlank() } ?: title, // itemId
                        "",                                         // customClasses
                        ""                                          // pageAnchorId
                    )
                }
                .filter { it.first }
                .map { it.second }
                .firstOrNull()
                ?: title
        }
    }

}
