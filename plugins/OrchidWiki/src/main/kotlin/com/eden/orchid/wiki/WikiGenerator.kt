package com.eden.orchid.wiki

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.FileCollection
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.generators.PageCollection
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.ImpliedKey
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.wiki.model.WikiModel
import com.eden.orchid.wiki.model.WikiSection
import com.eden.orchid.wiki.pages.WikiBookPage
import com.eden.orchid.wiki.pages.WikiSectionsPage
import com.eden.orchid.wiki.utils.WikiUtils
import javax.inject.Inject

@Description("Create a structured and navigable knowledge-base for your project.", name = "Wiki")
class WikiGenerator : OrchidGenerator<WikiModel>(GENERATOR_KEY, PRIORITY_EARLY) {

    companion object {
        const val GENERATOR_KEY = "wiki"
    }

    @Option
    @ImpliedKey("key")
    @Description("The sections within the baseDir to make wikis out of.")
    lateinit var sections: MutableList<WikiSection>

    @Option
    @Description("The configuration for the default wiki, when no other categories are set up.")
    lateinit var defaultConfig: WikiSection

    override fun startIndexing(context: OrchidContext): WikiModel {
        if (EdenUtils.isEmpty(sections)) {
            sections.add(defaultConfig)
        }

        val loadedSections = ArrayList<WikiSection>()
        this.sections.forEach { section ->
            val loadedSectionContent = section.adapter.loadWikiPages(section)
            if (loadedSectionContent != null) {
                val (summaryPage, wiki) = loadedSectionContent

                WikiUtils.linkWikiPages(summaryPage, wiki)

                section.summaryPage = summaryPage
                section.wikiPages = wiki
                section.bookPage = WikiUtils.createWikiPdf(section)

                loadedSections.add(section)
            }
        }

        val model = WikiModel(loadedSections)

        if (loadedSections.size > 1) {
            model.sectionsPage = getSectionsIndex(context, model)
        }

        return model
    }

    override fun startGeneration(context: OrchidContext, model: WikiModel) {
        model.allPages.forEach {
            if (it is WikiBookPage) {
                context.renderBinary(it)
            } else {
                context.renderTemplate(it)
            }
        }
    }

    private fun getSectionsIndex(context: OrchidContext, model: WikiModel): WikiSectionsPage {
        val resource = StringResource(context, "wiki.md", "")

        val sectionsPage = WikiSectionsPage(model, resource, "Wiki")

        for (summaryPage in model.sections.values) {
            summaryPage.summaryPage.sectionsPage = sectionsPage
            summaryPage.summaryPage.parent = sectionsPage
        }

        return sectionsPage
    }

    override fun getCollections(context: OrchidContext, model: WikiModel): List<OrchidCollection<*>> {
        val collectionsList = java.util.ArrayList<OrchidCollection<*>>()

        model.sections.forEach {
            val sectionPages = ArrayList<OrchidPage>()

            sectionPages.add(it.value.summaryPage)
            sectionPages.addAll(it.value.wikiPages)

            val collection = FileCollection(this, it.key, sectionPages)
            collectionsList.add(collection)
        }

        val bookPages = model.sections.values.mapNotNull { it.bookPage }
        collectionsList.add(PageCollection(this, "books", bookPages))

        return collectionsList
    }

}

