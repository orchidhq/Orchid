package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import java.util.ArrayList
import javax.inject.Inject

@Description(value = "Trim a String, array, or Iterable down to size.", name = "Limit-To")
class LimitToFunction @Inject
constructor() : TemplateFunction("limitTo", false) {

    @Option
    @Description("A String to limit the length of.")
    var input: Any? = null

    @Option
    @Description("The length to limit.")
    var count: Int = 0

    override fun parameters(): Array<String> {
        return arrayOf("input", "count")
    }

    @Suppress("UNCHECKED_CAST")
    override fun apply(): Any {
        if (count == 0) {
            throw IllegalArgumentException("Count must be given.")
        }

        if (input != null) {
            if (input is Iterable<*>) {
                val originalItems = input as Iterable<*>?
                val limitedItems = ArrayList<Any?>()
                var i = 0
                for (o in originalItems!!) {
                    if (i >= count) break
                    limitedItems.add(o)
                    i++
                }
                return limitedItems
            } else if (input!!.javaClass.isArray) {
                val originalItems = input as Array<Any>?
                val limitedItems = ArrayList<Any>()
                var i = 0
                for (o in originalItems!!) {
                    if (i >= count) break
                    limitedItems.add(o)
                    i++
                }
                return limitedItems
            } else {
                val actualInput = if (input is String) input as String? else this.input!!.toString()

                return actualInput!!.substring(0, count)
            }
        }
        return ""
    }
}
