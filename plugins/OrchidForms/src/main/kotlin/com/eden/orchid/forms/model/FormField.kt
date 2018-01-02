package com.eden.orchid.forms.model

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import org.json.JSONObject
import java.util.*
import javax.inject.Inject

abstract class FormField @Inject
constructor(val context: OrchidContext) : OptionsHolder {

    lateinit var key: String

    @Option @StringDefault("text")
    lateinit var type: String

    @Option
    var order: Int = 0

    @Option
    var label: String = ""
        get() {
            return if (!EdenUtils.isEmpty(field)) field else key
        }

    @Option
    var placeholder: String = ""
        get() {
            return if (!EdenUtils.isEmpty(field)) field else label
        }

    @Option
    lateinit var templates: Array<String>

    @Option("span") @StringDefault("auto")
    lateinit var spanVal: String

    @Option("spanSm") @StringDefault("auto")
    lateinit var spanSmVal: String

    @Option @BooleanDefault(false)
    var required: Boolean = false

    fun initialize(key: String, fieldData: JSONObject) {
        this.key = key
        extractOptions(context, fieldData)
    }

    open fun getTemplates(): List<String> {
        val allTemplates = ArrayList<String>()
        if (!EdenUtils.isEmpty(templates)) {
            Collections.addAll(allTemplates, *templates)
        }

        allTemplates.add("fields/" + key)
        allTemplates.add("fields/" + type)

        return allTemplates
    }

    fun getSpan(): String {
        var wrapperClasses = ""

        when (spanVal) {
            "right" -> wrapperClasses += "col-right "
            else -> wrapperClasses += "col "
        }

        when (spanVal) {
            "full" -> wrapperClasses += "col-lg-12 "
            "left" -> wrapperClasses += "col-lg-6 "
            "right" -> wrapperClasses += "col-lg-6 "
            "auto" -> wrapperClasses += "col-lg-6 "
            else -> try {
                val colSpan = Integer.parseInt(spanVal)
                wrapperClasses += "col-lg-$colSpan "
            } catch (e: NumberFormatException) {
                wrapperClasses += "col-lg-6 "
            }

        }
        when (spanSmVal) {
            "auto" -> wrapperClasses += "col-sm-12 "
            else -> try {
                val colSpan = Integer.parseInt(spanSmVal)
                wrapperClasses += "col-sm-$colSpan "
            } catch (e: NumberFormatException) {
                wrapperClasses += "col-sm-6 "
            }

        }

        return wrapperClasses
    }

    abstract fun acceptsType(type: String): Boolean

}
