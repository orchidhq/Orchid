package com.eden.orchid.taxonomies

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.ImpliedKey
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import com.eden.orchid.taxonomies.collections.TaxonomyLandingPagesCollection
import com.eden.orchid.taxonomies.collections.TaxonomyTermItemsCollection
import com.eden.orchid.taxonomies.collections.TaxonomyTermsLandingPagesCollection
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy
import com.eden.orchid.taxonomies.models.Term
import com.eden.orchid.taxonomies.pages.TaxonomyArchivePage
import com.eden.orchid.taxonomies.pages.TermArchivePage
import com.eden.orchid.taxonomies.utils.getSingleTermValue
import com.eden.orchid.taxonomies.utils.getTermValues
import javax.inject.Inject

@Description("Create custom archives from any logically-related content.", name = "Taxonomies")
class TaxonomiesGenerator
@Inject
constructor(
    val permalinkStrategy: PermalinkStrategy
) : OrchidGenerator<TaxonomiesModel>(GENERATOR_KEY, PRIORITY_DEFAULT) {

    companion object {
        const val GENERATOR_KEY = "taxonomies"
    }

    @Option
    @ImpliedKey("key")
    @Description("An array of Taxonomy configurations.")
    lateinit var taxonomies: List<Taxonomy>

    override fun startIndexing(context: OrchidContext): TaxonomiesModel {
        val model = TaxonomiesModel(context)

        if (!EdenUtils.isEmpty(taxonomies)) {
            taxonomies.forEach outerLoop@{ taxonomy ->
                model.putTaxonomy(taxonomy)
                val enabledGeneratorKeys = context.getGeneratorKeys(taxonomy.includeFrom, taxonomy.excludeFrom)

                context.index.getChildIndices(enabledGeneratorKeys)
                    .flatMap { it.allPages }
                    .forEach innerLoop@{ page ->
                        if (page.getSingleTermValue("skipTaxonomy") == "true") {
                            return@innerLoop
                        }

                        val pageTerms = HashSet<String?>()
                        if (taxonomy.single) {
                            pageTerms.add(page.getSingleTermValue(taxonomy.key))
                        } else {
                            if (taxonomy.singleKey.isNotBlank()) {
                                pageTerms.add(page.getSingleTermValue(taxonomy.singleKey))
                            }

                            pageTerms.addAll(page.getTermValues(taxonomy.key))
                        }

                        pageTerms.forEach { term ->
                            if (term != null) {
                                model.addPage(taxonomy, term, page)
                            }
                        }
                    }
            }
        }

        model.onIndexingTermsFinished()

        model.allPages = buildAllTaxonomiesPages(context, model)

        return model
    }

    override fun startGeneration(context: OrchidContext, model: TaxonomiesModel) {
        model.allPages.forEach { context.renderTemplate(it) }
    }

    override fun getCollections(context: OrchidContext, model: TaxonomiesModel): List<OrchidCollection<*>>? {
        val collections = ArrayList<OrchidCollection<*>>()

        // a collection containing landing pages for each Taxonomy
        collections.add(TaxonomyLandingPagesCollection(this, model))

        model.taxonomies.values.forEach { taxonomy ->
            // a collection containing landing pages for each Taxonomy's terms
            collections.add(TaxonomyTermsLandingPagesCollection(this, taxonomy))

            taxonomy.terms.values.forEach { term ->
                // a collection containing the individual items for each term
                collections.add(TaxonomyTermItemsCollection(this, taxonomy, term))
            }
        }

        return collections
    }

// Archive Page Helpers
//----------------------------------------------------------------------------------------------------------------------

    // build all pages for each taxonomy
    private fun buildAllTaxonomiesPages(context: OrchidContext, model: TaxonomiesModel): List<OrchidPage> {
        val archivePages = ArrayList<OrchidPage>()

        model.taxonomies.values.forEach { taxonomy ->
            buildTaxonomyLandingPages(context, model, taxonomy)


            taxonomy.allTerms.forEach { term ->
                archivePages.addAll(buildTermArchivePages(context, model, taxonomy, term))
            }
            archivePages.addAll(taxonomy.archivePages)
        }

        return archivePages
    }

    // build a set of pages that display all the terms in a given taxonomy
    private fun buildTaxonomyLandingPages(
        context: OrchidContext,
        model: TaxonomiesModel,
        taxonomy: Taxonomy
    ): List<OrchidPage> {
        val terms = taxonomy.allTerms
        val termPages = ArrayList<OrchidPage>()

        val pages = Math.ceil((taxonomy.terms.size / taxonomy.pageSize).toDouble()).toInt()

        for (i in 0..pages) {
            val termList = terms.subList(i * taxonomy.pageSize, Math.min((i + 1) * taxonomy.pageSize, terms.size))
            if (termList.isNotEmpty()) {
                var title = taxonomy.title
                if (i != 0) title += " (Page ${i + 1})"

                val pageRef = OrchidReference(context, "taxonomy.html")
                pageRef.title = title

                val page = TaxonomyArchivePage(StringResource("", pageRef), model, taxonomy, i + 1)

                permalinkStrategy.applyPermalink(page, page.taxonomy.permalink)

                termPages.add(page)
            }
        }

        linkPages(termPages)
        taxonomy.archivePages = termPages

        return termPages
    }

    // build a set of pages that display all the items in a given term within a taxonomy
    private fun buildTermArchivePages(
        context: OrchidContext,
        model: TaxonomiesModel,
        taxonomy: Taxonomy,
        term: Term
    ): List<OrchidPage> {
        val pagesList = term.allPages
        val termArchivePages = ArrayList<OrchidPage>()

        val pages = Math.ceil((pagesList.size / term.pageSize).toDouble()).toInt()

        for (i in 0..pages) {
            val termPageList = pagesList.subList(i * term.pageSize, Math.min((i + 1) * term.pageSize, pagesList.size))
            if (termPageList.isNotEmpty()) {
                var title = term.title
                if (i != 0) title += " (Page ${i + 1})"

                val pageRef = OrchidReference(context, "term.html")
                pageRef.title = title

                val page = TermArchivePage(StringResource("", pageRef), model, termPageList, taxonomy, term, i + 1)
                permalinkStrategy.applyPermalink(page, page.term.permalink)
                page.parent = taxonomy.landingPage
                termArchivePages.add(page)
            }
        }

        linkPages(termArchivePages.reversed())
        term.archivePages = termArchivePages

        if (taxonomy.setAsPageParent) {
            for (page in pagesList) {
                page.parent = term.landingPage
            }
        }

        return termArchivePages
    }

// Other Utils
//----------------------------------------------------------------------------------------------------------------------

    private fun linkPages(pages: List<OrchidPage>) {
        var i = 0
        for (post in pages) {
            if (next(pages, i) != null) {
                post.next = next(pages, i)
            }
            if (previous(pages, i) != null) {
                post.previous = previous(pages, i)
            }
            i++
        }
    }

    private fun previous(pages: List<OrchidPage>, i: Int): OrchidPage? {
        if (pages.size > 1) {
            if (i != 0) {
                return pages[i - 1]
            }
        }

        return null
    }

    private fun next(pages: List<OrchidPage>, i: Int): OrchidPage? {
        if (pages.size > 1) {
            if (i < pages.size - 1) {
                return pages[i + 1]
            }
        }

        return null
    }

}
