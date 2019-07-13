package com.eden.orchid.posts.utils

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.posts.model.Author
import com.eden.orchid.posts.model.PostsModel
import com.eden.orchid.utilities.resolve
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
class AuthorOptionExtractor
@Inject
constructor(
        private val contextProvider: Provider<OrchidContext>,
        private val converter: StringConverter
) : OptionExtractor<Author>(1000) {

    override fun acceptsClass(clazz: Class<*>): Boolean {
        return clazz == Author::class.java
    }

    fun getValue(value: Any): String {
        return converter.convert(String::class.java, value).second
    }

    override fun getOption(field: Field, sourceObject: Any, key: String): Author? {
        val postsModel: PostsModel = contextProvider.get().resolve()

        if (sourceObject is JSONObject) {
            val author = Author()
            author.extractOptions(contextProvider.get(), sourceObject.toMap())
            return author
        }
        else {
            val value = converter.convert(String::class.java, sourceObject)
            if (value.first) {
                return postsModel.getAuthorByName(value.second)
            }
        }

        return null
    }

    override fun getDefaultValue(field: Field): Author? {
        return null
    }

}

