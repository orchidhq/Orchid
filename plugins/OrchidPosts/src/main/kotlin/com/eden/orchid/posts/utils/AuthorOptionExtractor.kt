package com.eden.orchid.posts.utils

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.posts.model.Author
import com.eden.orchid.posts.model.PostsModel
import com.google.inject.Provider
import org.json.JSONObject
import java.lang.reflect.Field
import javax.inject.Inject

/**
 * ### Destination Types
 *
 * | Field Type   | Annotation     | Default Value              |
 * |--------------|----------------|----------------------------|
 * | String       | @StringDefault | Annotation value() or null |
 * | String[]     | none           | Empty String[]             |
 */
class AuthorOptionExtractor @Inject
constructor(private val contextProvider: Provider<OrchidContext>, private val converter: StringConverter, private val postsModel: PostsModel) : OptionExtractor<Author>(1000) {

    override fun acceptsClass(clazz: Class<*>): Boolean {
        return clazz == Author::class.java
    }

    fun getValue(`object`: Any): String {
        return converter.convert(`object`).second
    }

    override fun getOption(field: Field, options: JSONObject, key: String): Author? {
        if (options.has(key)) {

            if (options.get(key) is JSONObject) {
                val author = Author()
                author.extractOptions(contextProvider.get(), options.getJSONObject(key))
                return author
            } else {
                val value = converter.convert(options.get(key))
                if (value.first) {
                    return postsModel.getAuthorByName(value.second)
                }
            }
        }

        return getDefaultValue(field)
    }

    override fun getDefaultValue(field: Field): Author? {
        return null
    }

    override fun getList(field: Field, options: JSONObject, key: String): List<Author>? {
        return null
    }

    override fun getArray(field: Field, options: JSONObject, key: String): Array<Any>? {
        return null
    }
}

