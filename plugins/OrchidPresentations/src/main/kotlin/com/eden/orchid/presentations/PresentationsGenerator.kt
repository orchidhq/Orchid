package com.eden.orchid.presentations

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.presentations.model.Presentation
import com.eden.orchid.presentations.model.PresentationsModel
import com.eden.orchid.presentations.model.Slide
import java.util.stream.Stream
import javax.inject.Inject

@Description("Embed presentations and slide-decks in your pages using Deck.js.", name = "Presentations")
class PresentationsGenerator
@Inject
constructor(
        context: OrchidContext,
        private val model: PresentationsModel
) : OrchidGenerator(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_DEFAULT) {

    companion object {
        const val GENERATOR_KEY = "presentations"
    }

    @Option
    @StringDefault("presentations")
    @Description("The base directory in local resources to look for presentation slides in.")
    lateinit var baseDir: String

    override fun startIndexing(): List<OrchidPage> {
        val presentations = HashMap<String, Presentation>()
        val resourceMap = getPresentationResources(baseDir)

        resourceMap.forEach { key, resources ->
            val presentation = getPresentation(key, resources)
            presentations.put(key, presentation)
        }

        model.initialize(presentations)

        return emptyList()
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {

    }

    private fun getPresentationResources(baseDir: String): Map<String, List<OrchidResource>> {
        val slides = context.getLocalResourceEntries(baseDir, context.compilerExtensions.toTypedArray(), true)
        val slideMap = HashMap<String, ArrayList<OrchidResource>>()

        slides.map {
            val key = it.reference.getPathSegment(1)

            it.reference.stripFromPath(baseDir)
            it.reference.stripFromPath(key)

            if (!slideMap.containsKey(key)) {
                slideMap.put(key, ArrayList())
            }
            slideMap.get(key)!!.add(it)
        }

        return slideMap
    }

    private fun getPresentation(section: String, resources: List<OrchidResource>): Presentation {
        val slides = resources
                .mapIndexed { index, orchidResource -> Slide(orchidResource, index) }
                .sortedBy { it.order }

        return Presentation(section, slides, "")
    }
}

