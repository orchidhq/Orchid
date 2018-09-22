package com.eden.orchid.api.indexing

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.utilities.OrchidUtils
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import java.util.Arrays

open class OrchidIndex(val parent: OrchidIndex?, val ownKey: String) {
    val ownPages = ArrayList<OrchidPage>()
    val childrenPages = LinkedHashMap<String, OrchidIndex>()

    open val allPages: List<OrchidPage>
        get() {
            val allPages = ArrayList<OrchidPage>()
            allPages.addAll(getOwnPages())

            for ((_, value) in childrenPages) {
                allPages.addAll(value.allPages)
            }

            return allPages
        }

    val children: Map<String, OrchidIndex>
        get() = childrenPages

    open fun addToIndex(taxonomy: String, page: OrchidPage) {
        val pathPieces = OrchidUtils.normalizePath(taxonomy).split("/").toTypedArray()

        if (!EdenUtils.isEmpty(pathPieces)) {
            addToIndex(pathPieces, page)
        }
    }

    fun addToIndex(pathPieces: Array<String>, page: OrchidPage) {
        // we have a path to add to this index
        if (!EdenUtils.isEmpty(pathPieces) && !EdenUtils.isEmpty(pathPieces[0])) {

            // we are at the correct index, because our own key is the current first level of `pathPieces`
            if (pathPieces[0] == ownKey) {
                // this is the final piece of the path, add it here
                if (pathPieces.size == 1) {
                    this.ownPages.add(page)
                }
                else {
                    val nextPathPiece = pathPieces[1]

                    // we haven't created an index for the next path piece, create one by reflection
                    if (!childrenPages.containsKey(nextPathPiece)) {
                        val indexInstance = OrchidIndex(this, nextPathPiece)
                        childrenPages[nextPathPiece] = indexInstance
                    }

                    // get the child index at the next path level and add the page there
                    childrenPages[nextPathPiece]!!.addToIndex(Arrays.copyOfRange(pathPieces, 1, pathPieces.size), page)
                }
            }
        }
    }

    open fun find(taxonomy: String): List<OrchidPage> {
        return find(taxonomy.split("/").toTypedArray())
    }

    fun find(pathPieces: Array<String>): List<OrchidPage> {
        val foundPages = ArrayList<OrchidPage>()
        // we have a path to search
        if (!EdenUtils.isEmpty(pathPieces) && !EdenUtils.isEmpty(pathPieces[0])) {
            if (pathPieces.size == 1 && pathPieces[0] == ownKey) {
                foundPages.addAll(this.allPages)
            }
            else {
                if (childrenPages.containsKey(pathPieces[1])) {
                    foundPages.addAll(childrenPages[pathPieces[1]]!!.find(Arrays.copyOfRange(pathPieces, 1, pathPieces.size)))
                }
            }
        }

        return foundPages
    }

    open fun findPage(taxonomy: String): OrchidPage? {
        return findPage(taxonomy.split("/").toTypedArray())
    }

    fun findPage(pathPieces: Array<String>): OrchidPage? {
        var page: OrchidPage? = null
        // we have a path to search
        if (!EdenUtils.isEmpty(pathPieces) && !EdenUtils.isEmpty(pathPieces[0])) {
            if (pathPieces.size == 1 && pathPieces[0] == ownKey) {
                for (ownPage in getOwnPages()) {
                    if (ownPage.reference.originalFileName == pathPieces[0]) {
                        page = ownPage
                        break
                    }
                }
            }
            else {
                if (childrenPages.containsKey(pathPieces[1])) {
                    page = childrenPages[pathPieces[1]]!!.findPage(Arrays.copyOfRange(pathPieces, 1, pathPieces.size))
                }
            }
        }

        return page
    }

    open fun findIndex(taxonomy: String): OrchidIndex? {
        return findIndex(taxonomy.split("/").toTypedArray())
    }

    fun findIndex(pathPieces: Array<String>): OrchidIndex? {
        var foundIndex: OrchidIndex? = null
        // we have a path to search
        if (!EdenUtils.isEmpty(pathPieces) && !EdenUtils.isEmpty(pathPieces[0])) {
            if (pathPieces.size == 1 && pathPieces[0] == ownKey) {
                foundIndex = this
            }
            else if (childrenPages.containsKey(pathPieces[1])) {
                foundIndex = childrenPages[pathPieces[1]]!!.findIndex(Arrays.copyOfRange(pathPieces, 1, pathPieces.size))
            }
        }

        return foundIndex
    }

    fun getOwnPages(): List<OrchidPage> {
        return ownPages.filter { page -> !page.isDraft }
    }

    fun addAll(index: OrchidIndex) {
        for (page in index.allPages) {
            this.addToIndex(page.reference.path, page)
        }
    }

    operator fun get(key: String): OrchidIndex? {
        return childrenPages[key]
    }

    @JvmOverloads
    fun toJSON(includePageContent: Boolean = false, includePageData: Boolean = false): JSONObject {
        val indexJson = JSONObject()
        indexJson.put("ownKey", ownKey)
        val ownPages = getOwnPages()
        if (ownPages.size > 0) {
            val ownPagesJson = JSONArray()
            for (page in ownPages) {
                ownPagesJson.put(page.toJSON(includePageContent, includePageData))
            }
            indexJson.put("ownPages", ownPagesJson)
        }

        if (childrenPages.keys.size > 0) {
            val childrenPagesJson = JSONObject()
            for ((key, value) in childrenPages) {
                childrenPagesJson.put(key, value.toJSON(includePageContent, includePageData))
            }
            indexJson.put("childrenPages", childrenPagesJson)
        }

        return indexJson
    }

    override fun toString(): String {
        return Clog.format("index [{}] with {} own pages and {} child indices",
                this.ownKey,
                ownPages.size,
                childrenPages.size
        )
    }

// Factory methods
//----------------------------------------------------------------------------------------------------------------------

    companion object {

        @JvmStatic
        fun from(rootKey: String, pages: Collection<OrchidPage>): OrchidIndex {
            val index = OrchidIndex(null, rootKey)
            for (page in pages) {
                val ref = OrchidReference(page.reference)
                index.addToIndex(ref.path, page)
            }

            return index
        }

        @JvmStatic
        fun fromJSON(context: OrchidContext, source: JSONObject): OrchidIndex {
            val index = OrchidIndex(null, source.getString("ownKey"))

            if (source.has("ownPages")) {
                val ownPagesJson = source.getJSONArray("ownPages")
                for (i in 0 until ownPagesJson.length()) {
                    val externalPage = OrchidPage.fromJSON(context, ownPagesJson.getJSONObject(i))
                    index.ownPages.add(externalPage)
                }
            }

            if (source.has("childrenPages")) {
                val childrenPagesJson = source.getJSONObject("childrenPages")
                for (key in childrenPagesJson.keySet()) {
                    val childIndex = OrchidIndex.fromJSON(context, childrenPagesJson.getJSONObject(key))
                    index.childrenPages[key] = childIndex
                }
            }

            return index
        }
    }
}
