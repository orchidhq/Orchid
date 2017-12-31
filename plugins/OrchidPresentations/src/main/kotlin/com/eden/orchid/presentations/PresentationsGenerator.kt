package com.eden.orchid.presentations

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.presentations.model.Presentation
import com.eden.orchid.presentations.model.PresentationsModel
import com.eden.orchid.presentations.model.Slide
import com.eden.orchid.utilities.OrchidUtils
import java.util.*
import java.util.stream.Stream
import javax.inject.Inject

@Description("Embed presentations and slide-decks in your pages.")
class PresentationsGenerator
@Inject constructor(context: OrchidContext, private val model: PresentationsModel)
    : OrchidGenerator(context, "presentations", 25) {

    @Option("baseDir") @StringDefault("presentations")
    @Description("The base directory to look for presentation slides in.")
    lateinit var presentationsBaseDir: String

    @Option("presentations")
    @Description("An array of Presentation keys. Presentation Components must specify a Presentation that exists " +
            "within this array, and slides for this Presentation come from a folder within the base directory given " +
            "by the presentation key."
    )
    lateinit var sectionNames: Array<String>

    override fun startIndexing(): List<OrchidPage>? {
        val presentations = HashMap<String, Presentation>()

        if (!EdenUtils.isEmpty(sectionNames)) {
            for (section in sectionNames) {
                val presentation = getPresentation(section)
                presentations.put(section, presentation)
            }
        }

        model.initialize(presentations)

        return null
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {

    }

    private fun getPresentation(section: String): Presentation {
        val path = OrchidUtils.normalizePath(presentationsBaseDir) + "/" + section
        val slides = context.getLocalResourceEntries(path , null, false).map { Slide(it) }

        return Presentation(section, slides, "")
    }
}
