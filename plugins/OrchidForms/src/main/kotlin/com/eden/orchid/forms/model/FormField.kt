package com.eden.orchid.forms.model

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.server.annotations.Extensible
import com.eden.orchid.api.theme.components.ModularPageListItem
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.Collections
import javax.inject.Inject

@Extensible
@Description("A field to be included in a dynamically-generated form.", name = "Form Fields")
abstract class FormField
@Inject
constructor(
    val inputTypes: Array<String>
) : ModularPageListItem<FormFieldList, FormField> {

    lateinit var containingPage: OrchidPage

    @Option
    @Description("The key the field maps to in the resulting form")
    lateinit var key: String

    @Option("type")
    @Description("The field type.")
    lateinit var fieldType: String

    @Option("order")
    @Description(
        "Form fields are defined in maps which do not have a defined order, and so order in which fields " +
                "are rendered are not necessarily the order they are declared. Set this property to manually define the " +
                "ordering of each field."
    )
    var orderNum: Int = 0

    @Option
    @Description("The user-facing label of this field.")
    var label: String = ""
        get() {
            return if (!EdenUtils.isEmpty(field)) field else key
        }

    @Option
    @Description("The user-facing placeholder of this field.")
    var placeholder: String = ""
        get() {
            return if (!EdenUtils.isEmpty(field)) field else label
        }

    @Option
    @Description(
        "A list of templates to use for this field, which takes precedence over the normal template. The " +
                "first template in this lit that exists will be chosen, otherwise falling back to the default if the " +
                "list is empty or none of the templates are found."
    )
    lateinit var template: Array<String>

    @Option("span")
    @StringDefault("auto")
    @Description(
        "The number of columns this field should occupy on large screens, out of 12. Can also be 'left' (6" +
                "columns, ordered from the left), 'right' (6 columns, ordered from the right), 'auto' (equivalent to " +
                "'left'), or 'full' (12 columns) The default is 'auto'."
    )
    lateinit var spanVal: String

    @Option("spanSm")
    @StringDefault("auto")
    @Description(
        "The number of columns this field should occupy on small screens, out of 12. Can also be 'left', " +
                "'right', 'auto', or 'full', which are all 12 columns. The default is 'auto'."
    )
    lateinit var spanSmVal: String

    @Option
    @BooleanDefault(false)
    @Description("Whether this field is required for submission.")
    var required: Boolean = false

    override fun initialize(context: OrchidContext, containingPage: OrchidPage) {
        this.containingPage = containingPage
    }

    open fun getTemplates(): List<String> {
        val allTemplates = ArrayList<String>()
        if (!EdenUtils.isEmpty(template)) {
            Collections.addAll(allTemplates, *template)
        }

        allTemplates.add("fields/$type-$key")
        allTemplates.add("fields/$key")
        allTemplates.add("fields/$type")

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

    fun setSpan(span: String) {
        this.spanVal = span
    }

    fun setSpanSm(span: String) {
        this.spanSmVal = span
    }

    fun acceptsType(type: String): Boolean {
        return inputTypes.contains(type)
    }

    override fun setOrder(order: Int) {
        orderNum = order
    }

    override fun getOrder(): Int {
        return orderNum
    }

    override fun getType(): String {
        return fieldType
    }

    fun setType(type: String) {
        this.fieldType = type
    }

    override fun canBeUsedOnPage(
        containingPage: OrchidPage,
        modularList: FormFieldList,
        possibleItems: List<Map<String, Any>>,
        currentItems: MutableList<FormField>
    ): Boolean {
        return true
    }
}
