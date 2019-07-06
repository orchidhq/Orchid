package com.eden.orchid.kss

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.kss.model.KssModel
import com.eden.orchid.kss.pages.KssPage
import com.eden.orchid.kss.parser.KssParser
import com.eden.orchid.utilities.OrchidUtils

@Description("Generate a living styleguide for your CSS using Knyle Style Sheets (KSS).", name = "Living Styleguide")
class KssGenerator : OrchidGenerator<KssModel>(GENERATOR_KEY, PRIORITY_EARLY) {

    companion object {
        const val GENERATOR_KEY = "styleguide"
    }

    @Option
    @StringDefault("assets/css")
    @Description("The base directory in local resources to look for KSS blocks in.")
    lateinit var baseDir: String

    @Option
    @Description("The sections within the baseDir to make Styleguides out of.")
    var sections: Array<String> = emptyArray()

    @Option
    @Description("The stylesheet URL that you are documenting. This is included in the example markup within a Shadow" +
            "element (if the brower supports it)."
    )
    lateinit var stylesheet: String

    override fun startIndexing(context: OrchidContext): KssModel {
        val indexedSections: MutableMap<String?, List<KssPage>> = LinkedHashMap()

        if (EdenUtils.isEmpty(sections)) {
            indexedSections.put(null, getStyleguidePages(context, null))
        }
        else {
            for (section in sections) {
                indexedSections.put(section, getStyleguidePages(context, section))
            }
        }

        return KssModel(indexedSections)
    }

    private fun getStyleguidePages(context: OrchidContext, section: String?): List<KssPage> {
        val sectionBaseDir = if (!EdenUtils.isEmpty(section))
            OrchidUtils.normalizePath(baseDir) + "/" + OrchidUtils.normalizePath(section)
        else
            OrchidUtils.normalizePath(baseDir)

        val pages = ArrayList<KssPage>()

        val resources = context.getLocalResourceEntries(sectionBaseDir, arrayOf("css", "sass", "scss", "less"), true)
        val parser = KssParser(resources)
        parser.sections.values.forEach {
            val page = KssPage(context, it)
            if (!EdenUtils.isEmpty(stylesheet)) {
                page.stylesheet = stylesheet
            }

            pages.add(page)
        }

        pages.forEach { page ->
            pages.forEach { otherPage ->
                if (page.styleguideSection.depth > 1 && page.styleguideSection.parent != null) {
                    // find parent of [page]
                    if (otherPage.styleguideSection == page.styleguideSection.parent) {
                        page.parent = otherPage
                    }
                }

                // also find children of [page]
                if (otherPage.styleguideSection.parent != null && otherPage.styleguideSection.parent == page.styleguideSection) {
                    page.children.add(otherPage)
                }
            }
        }

        return pages
    }

    override fun startGeneration(context: OrchidContext, model: KssModel) {
        model.allPages.forEach { context.renderTemplate(it) }
    }

    override fun getCollections(context: OrchidContext, model: KssModel): List<OrchidCollection<*>> {
        return emptyList()
    }

}
