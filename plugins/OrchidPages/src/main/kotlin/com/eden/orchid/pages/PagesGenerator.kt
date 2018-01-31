package com.eden.orchid.pages


import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.FileCollection
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.pages.pages.StaticPage
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description("Generates static pages with the same output folder as their input, minus the base directory. Input " +
        "pages come from 'baseDir' option value, which defaults to 'pages'."
)
class PagesGenerator @Inject
constructor(context: OrchidContext) : OrchidGenerator(context, "pages", OrchidGenerator.PRIORITY_EARLY) {

    @Option @StringDefault("pages")
    lateinit var baseDir: String

    override fun startIndexing(): List<OrchidPage> {
        val resourcesList = context.getLocalResourceEntries(baseDir, null, true)
        val pages = ArrayList<StaticPage>()

        for (entry in resourcesList) {
            entry.reference.stripFromPath(baseDir)
            val page = StaticPage(entry)
            pages.add(page)
        }

        return pages
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { if (it is StaticPage) { context.render(it, it.renderMode) } }
    }

    override fun getCollections(): List<OrchidCollection<*>> {
        return listOf<OrchidCollection<*>>(FileCollection(this, "pages", context.getGeneratorPages(this.key)))
    }
}
