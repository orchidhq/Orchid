package com.eden.orchid.taxonomies


import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.taxonomies.models.TaxonomiesModel
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
        val pages = ArrayList<OrchidPage>()
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

        model.taxonomies.values.forEach { taxonomy ->
            Clog.i("Taxonomy: ${taxonomy.key}")
            taxonomy.terms.values.forEach { term ->
                Clog.v("Taxonomy term: ${term.key} (${term.pages.size} pages)")
            }
        }

        return pages
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { context.renderTemplate(it) }
    }

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

}
