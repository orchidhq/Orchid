package com.eden.orchid.kss

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.kss.model.KssModel
import com.eden.orchid.kss.pages.KssPage
import com.eden.orchid.kss.parser.KssParser
import com.eden.orchid.utilities.OrchidUtils
import java.util.stream.Stream
import javax.inject.Inject

@Description("Generate a living styleguide for your CSS using Knyle Style Sheets (KSS).", name = "Living Styleguide")
class KssGenerator
@Inject
constructor(
        context: OrchidContext,
        val model: KssModel
) : OrchidGenerator(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_EARLY) {

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

    override fun startIndexing(): List<OrchidPage> {
        model.initialize()

        if (EdenUtils.isEmpty(sections)) {
            model.sections.put(null, getStyleguidePages(null))
        }
        else {
            for (section in sections) {
                model.sections.put(section, getStyleguidePages(section))
            }
        }

        return model.getAllPages()
    }

    private fun getStyleguidePages(section: String?): List<KssPage> {
        val sectionBaseDir = if (!EdenUtils.isEmpty(section))
            OrchidUtils.normalizePath(baseDir) + "/" + OrchidUtils.normalizePath(section)
        else
            OrchidUtils.normalizePath(baseDir)

        val pages = ArrayList<KssPage>()

        val resources = context.getLocalResourceEntries(sectionBaseDir, arrayOf("css", "sass", "scss", "less"), true)
        val parser = KssParser(resources)
        parser.sections.values.forEach {
            val page = KssPage(this.context, it)
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

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { context.renderTemplate(it) }
    }

    override fun getCollections(pages: List<OrchidPage>): List<OrchidCollection<*>> {
        return emptyList()
    }

}
