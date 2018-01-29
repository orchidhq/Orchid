package com.eden.orchid.taxonomies


import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.taxonomies.models.TaxonomiesModel
import com.eden.orchid.taxonomies.models.Taxonomy
import com.eden.orchid.taxonomies.models.TaxonomyPaginator
import com.eden.orchid.taxonomies.models.Term
import com.eden.orchid.taxonomies.pages.TaxonomyArchivePage
import com.eden.orchid.taxonomies.pages.TermArchivePage
import org.json.JSONArray
import org.json.JSONObject
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description("Generates static pages with the same output folder as their input, minus the base directory. Input " +
        "pages come from 'baseDir' option value, which defaults to 'pages'."
)
class TaxonomiesGenerator @Inject
constructor(context: OrchidContext, val model: TaxonomiesModel) : OrchidGenerator(context, "taxonomies", OrchidGenerator.PRIORITY_DEFAULT) {

    @Option
    lateinit var singleTypes: Array<String>

    @Option
    lateinit var multiTypes: Array<String>

    override fun startIndexing(): List<OrchidPage> {
        model.initialize()

        if(!EdenUtils.isEmpty(singleTypes) || !EdenUtils.isEmpty(multiTypes)) {
            context.internalIndex.allPages.forEach { page ->
                if(!EdenUtils.isEmpty(singleTypes)) {
                    singleTypes.forEach { taxonomy ->
                        val term = getTermFromPage(taxonomy, page)

                        if(term != null) {
                            model.addPage(taxonomy, term, page)
                        }
                    }
                }

                if(!EdenUtils.isEmpty(multiTypes)) {
                    multiTypes.forEach { taxonomy ->
                        val terms = getTermsFromPage(taxonomy, page)

                        terms.forEach { term ->
                            model.addPage(taxonomy, term, page)
                        }
                    }
                }
            }
        }

        return buildAllTaxonomiesPages()
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { context.renderTemplate(it) }
    }

// Taxonomy Indexing Helpers
//----------------------------------------------------------------------------------------------------------------------

    private fun getTermFromPage(taxonomy: String, page: OrchidPage): String? {
        try {
            val method = page.javaClass.getMethod("get${taxonomy.capitalize()}")
            return method.invoke(page) as String
        }
        catch (e: Exception) {
            if(page.allData.element is JSONObject) {
                val pageData = page.allData.element as JSONObject

                if(pageData.has(taxonomy) && pageData.get(taxonomy) is String) {
                    return pageData.getString(taxonomy)
                }
            }
        }

        return null
    }

    private fun getTermsFromPage(taxonomy: String, page: OrchidPage): List<String> {
        try {
            val method = page.javaClass.getMethod("get${taxonomy.capitalize()}")
            return method.invoke(page) as List<String>
        }
        catch (e: Exception) {
            if(page.allData.element is JSONObject) {
                val pageData = page.allData.element as JSONObject

                if(pageData.has(taxonomy) && pageData.get(taxonomy) is JSONArray) {
                    val terms = ArrayList<String>()

                    pageData.getJSONArray(taxonomy).forEach {
                        terms.add(it.toString())
                    }

                    return terms
                }
            }
        }

        return emptyList()
    }

// Archive Page Helpers
//----------------------------------------------------------------------------------------------------------------------

    // build all pages for each taxonomy
    private fun buildAllTaxonomiesPages(): List<OrchidPage> {
        val archivePages = ArrayList<OrchidPage>()

        model.taxonomies.values.forEach { taxonomy ->
            taxonomy.terms.values.forEach { term ->
                archivePages.addAll(buildTermArchivePages(taxonomy, term, getPaginator()))
            }
            archivePages.addAll(buildTaxonomyLandingPages(taxonomy, getPaginator()))
        }

        return archivePages
    }

    // build a set of pages that display all the terms in a given taxonomy
    private fun buildTaxonomyLandingPages(taxonomy: Taxonomy, paginator: TaxonomyPaginator): List<OrchidPage> {
        val terms = taxonomy.terms.values.toList()
        val termPages = ArrayList<OrchidPage>()

        val pages = Math.ceil((taxonomy.terms.size / paginator.pageSize).toDouble()).toInt()

        for (i in 0..pages) {
            val termList = terms.subList(i * paginator.pageSize, Math.min((i + 1) * paginator.pageSize, terms.size))
            if (termList.isNotEmpty()) {
                val permalink = taxonomy.key + (if(i == 0) ".html" else "/${i + 1}.html")
                var title = "Taxonomy ${taxonomy.key.capitalize()}"
                if (i == 0) title += " (Page ${i+1})"

                val pageRef = OrchidReference(context, permalink)
                pageRef.title = title

                val page = TaxonomyArchivePage(StringResource("", pageRef), model, taxonomy, i + 1)
                termPages.add(page)
            }
        }

        linkPages(termPages)
        taxonomy.archivePages = termPages

        return termPages
    }

    // build a set of pages that display all the items in a given term within a taxonomy
    private fun buildTermArchivePages(taxonomy: Taxonomy, term: Term, paginator: TaxonomyPaginator): List<OrchidPage> {
        val pagesList = term.pages.toList()
        val termArchivePages = ArrayList<OrchidPage>()

        val pages = Math.ceil((pagesList.size / paginator.pageSize).toDouble()).toInt()

        for (i in 0..pages) {
            val termPageList = pagesList.subList(i * paginator.pageSize, Math.min((i + 1) * paginator.pageSize, pagesList.size))
            if (termPageList.isNotEmpty()) {
                val permalink = "${taxonomy.key}/${term.key}" + (if(i == 0) ".html" else "/${i + 1}.html")
                var title = "Taxonomy ${taxonomy.key.capitalize()} - ${term.key.capitalize()}"
                if (i == 0) title += " (Page ${i+1})"

                val pageRef = OrchidReference(context, permalink)
                pageRef.title = title

                val page = TermArchivePage(StringResource("", pageRef), model, termPageList, taxonomy, term, i + 1)
                termArchivePages.add(page)
            }
        }

        linkPages(termArchivePages)
        term.archivePages = termArchivePages

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

    private fun getPaginator(): TaxonomyPaginator {
        val paginator = TaxonomyPaginator()
        paginator.extractOptions(context, JSONObject())
        return paginator
    }

}
