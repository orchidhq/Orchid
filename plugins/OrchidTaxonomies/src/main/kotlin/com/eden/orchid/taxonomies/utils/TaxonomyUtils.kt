package com.eden.orchid.taxonomies.utils

import com.eden.orchid.api.theme.pages.OrchidPage
import org.json.JSONArray
import org.json.JSONObject

fun OrchidPage.getSingleTermValue(taxonomy: String): String? {
    try {
        val method = this.javaClass.getMethod("get${taxonomy.capitalize()}")
        return method.invoke(this) as String
    } catch (e: Exception) {
        if (this.allData.element is JSONObject) {
            val pageData = this.allData.element as JSONObject

            if (pageData.has(taxonomy) && pageData.get(taxonomy) is String) {
                return pageData.getString(taxonomy)
            }
        }
    }

    return null
}

@Suppress("UNCHECKED_CAST")
fun OrchidPage.getTermValues(taxonomy: String): List<String> {
    try {
        val method = this.javaClass.getMethod("get${taxonomy.capitalize()}")
        return method.invoke(this) as List<String>
    } catch (e: Exception) {
        if (this.allData.element is JSONObject) {
            val pageData = this.allData.element as JSONObject

            if (pageData.has(taxonomy) && pageData.get(taxonomy) is JSONArray) {
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
