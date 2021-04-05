package com.eden.orchid.taxonomies.models

import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.AllOptions
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.annotations.Validate
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.taxonomies.utils.getSingleTermValue
import com.eden.orchid.utilities.camelCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.titleCase
import com.eden.orchid.utilities.to
import org.json.JSONObject
import javax.inject.Inject
import jakarta.validation.constraints.NotBlank

@Validate
class CollectionArchive
@Inject
constructor(
    val context: OrchidContext
) : OptionsHolder {

    public var pages = listOf<OrchidPage>()
    lateinit var archivePages: List<OrchidPage>

    @Option
    @get:NotBlank
    var key: String = ""
        get() {
            return when {
                field.isNotBlank() -> field
                collectionId.isNotBlank() -> collectionId from { camelCase() } to { titleCase() }
                collectionType.isNotBlank() -> collectionType from { camelCase() } to { titleCase() }
                else -> ""
            }
        }

    @Option
    lateinit var collectionType: String

    @Option
    lateinit var collectionId: String

    @Option
    lateinit var merge: List<CollectionParameters>

    @AllOptions
    lateinit var allData: Map<String, Any>

    @Option
    @IntDefault(100)
    @Description("The maximum number of term pages to include in a single page in the Taxonomy archive.")
    var pageSize: Int = 100

    @Option
    @StringDefault(":collectionKey/:archiveIndex")
    @Description("The permalink structure to use for this taxonomy's archive pages.")
    lateinit var permalink: String

    @Option
    @Description(
        "If true, pages selected by this taxonomy will have their parent page be set as the term archive, " +
                "creating a complete breadcrumb hierarchy. \n\n" +
                "**WARNING**: This will override the parent page set by the plugin that originally produced the page, " +
                "effectively replacing its normal hierarchy with this taxonomy's hierarchy."
    )
    var setAsPageParent: Boolean = false

    @Option
    @Description("A list of properties to order the Terms by.")
    lateinit var orderBy: Array<String>

    @Option
    @StringDefault("desc")
    @Description("Whether to sort in ascending or descending order. One of [asc, desc].")
    lateinit var orderByDirection: String

    @Option
    @Description("The displayed title of the Taxonomy. Defaults to the un-camelCased Taxonomy key.")
    var title: String = ""
        get() {
            return when {
                field.isNotBlank() -> field
                key.isNotBlank() -> key from { camelCase() } to { titleCase() }
                collectionId.isNotBlank() -> collectionId from { camelCase() } to { titleCase() }
                collectionType.isNotBlank() -> collectionType from { camelCase() } to { titleCase() }
                else -> ""
            }
        }

    val landingPage: OrchidPage
        get() {
            return archivePages.first()
        }

    val link: String
        get() {
            return landingPage.link
        }

    var allPages: List<OrchidPage> = ArrayList()
        private set
        get() {
            if(field.isEmpty() && pages.isNotEmpty()) {
                var sortedList = pages.toList()

                var comparator: Comparator<OrchidPage>? = null
                if (orderBy.size > 0) {
                    orderBy.forEach { prop ->
                        comparator = if (comparator == null)
                            compareBy { it.getSingleTermValue(prop) }
                        else
                            comparator!!.thenBy { it.getSingleTermValue(prop) }

                    }
                } else {
                    comparator = compareBy<OrchidPage> { it.publishDate }.thenBy { it.title }
                }

                if (orderByDirection.equals("desc", ignoreCase = true)) {
                    comparator = comparator!!.reversed()
                }

                field = sortedList.sortedWith(comparator!!)
            }
            return field
        }

    fun query(pointer: String): JSONElement? {
        return JSONElement(JSONObject(allData)).query(pointer)
    }

    class CollectionParameters : OptionsHolder {
        @Option
        lateinit var collectionType: String

        @Option
        lateinit var collectionId: String
    }

}
