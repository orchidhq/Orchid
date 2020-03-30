package com.eden.orchid.forms.model

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.AllOptions
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.ImpliedKey
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.impl.relations.PageRelation
import org.json.JSONObject

class Form(
    val context: OrchidContext,
    var key: String,
    formData: Map<String, Any>
) : OptionsHolder {

    @Option
    @Description("The user-facing title of the form.")
    lateinit var title: String

    @Option
    @Description("The 'action' attribute of the form. On forms which have a dedicated submission page, if this is " +
            "not provided, it defaults to the URL of the submission page."
    )
    var action: String = ""
    get() {
        return if(field.isNotBlank()) {
            field
        }
        else {
            redirectionPage.get()?.link ?: ""
        }
    }

    @Option @StringDefault("post")
    @Description("The HTTP request method the form submits as. One of ['get', 'post'].")
    lateinit var method: String

    @Option @StringDefault("__method")
    @Description("The name of a hidden field which holds the redirection page URL")
    lateinit var methodFieldName: String

    @Option
    @Description("A map of arbitrary attributes to add to the form element.")
    lateinit var attributes: JSONObject

    @Option @ImpliedKey(typeKey = "key")
    @Description("The fields in this form.")
    var fields: FormFieldList? = null
    get() {
        if(redirectionPage.get() != null) {
            field?.addExtraField("redirectionPage", redirectionPageFieldName, redirectionPage.get()!!.link)
        }
        if(!method.equals("get", ignoreCase = true) && !method.equals("post", ignoreCase = true)) {
            field?.addExtraField("method", methodFieldName, method)
        }
        return field
    }

    @Option
    @Description("The page to redirect to after a successful submission")
    lateinit var redirectionPage: PageRelation

    @Option @StringDefault("__onSubmit")
    @Description("The name of a hidden field which holds the redirection page URL")
    lateinit var redirectionPageFieldName: String

    @AllOptions
    lateinit var allData: Map<String, Any>

    init {
        try {
            extractOptions(context, formData)

            if(EdenUtils.isEmpty(key) && !EdenUtils.isEmpty(title)) {
                key = title
            }

            if(EdenUtils.isEmpty(key)) {
                throw IllegalArgumentException("The form must define a 'key' or a 'title'.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val attrs: Map<String, Any>
    get() {
        return attributes.toMap()
    }

}
