package com.eden.orchid.presentations

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.presentations.model.Presentation
import com.eden.orchid.presentations.model.PresentationsModel
import com.eden.orchid.presentations.model.Slide
import javax.inject.Inject

@Description("Embed presentations and slide-decks in your pages using Deck.js.", name = "Presentations")
class PresentationsGenerator : OrchidGenerator<PresentationsModel>(GENERATOR_KEY, PRIORITY_DEFAULT) {

    companion object {
        const val GENERATOR_KEY = "presentations"
    }

    @Option
    @StringDefault("presentations")
    @Description("The base directory in local resources to look for presentation slides in.")
    lateinit var baseDir: String

    override fun startIndexing(context: OrchidContext): PresentationsModel {
        return PresentationsModel(
            getPresentationResources(context, baseDir)
                .mapValues { getPresentation(key, it.value) }
        )
    }

    override fun startGeneration(context: OrchidContext, model: PresentationsModel) {

    }

    private fun getPresentationResources(context: OrchidContext, baseDir: String): Map<String, List<OrchidResource>> {
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

