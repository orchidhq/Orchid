package com.eden.orchid.pages


import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.pages.pages.StaticPage
import java.util.*
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description("Generates static pages with the same output folder as their input, minus the base directory. Input " +
        "pages come from 'baseDir' option value, which defaults to 'pages'."
)
class PagesGenerator @Inject
constructor(context: OrchidContext) : OrchidGenerator(context, "pages", 700) {

    @Option("baseDir") @StringDefault("pages")
    lateinit var pagesBaseDir: String

    override fun startIndexing(): List<OrchidPage> {
        val resourcesList = context.getLocalResourceEntries(pagesBaseDir, null, true)
        val pages = ArrayList<StaticPage>()

        for (entry in resourcesList) {
            entry.reference.stripFromPath(pagesBaseDir)
            val page = StaticPage(entry)
            pages.add(page)
        }

        return pages
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { if (it is StaticPage) { context.render(it, it.renderMode) } }
    }
}
