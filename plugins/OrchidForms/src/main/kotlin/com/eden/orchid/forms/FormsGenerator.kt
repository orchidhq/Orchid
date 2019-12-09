package com.eden.orchid.forms

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.forms.model.Form
import com.eden.orchid.forms.model.FormsModel
import com.eden.orchid.utilities.OrchidUtils

@Description("Indexes form definitions so they can be easily referenced from components on different pages.",
        name = "Forms"
)
class FormsGenerator : OrchidGenerator<FormsModel>(GENERATOR_KEY, PRIORITY_DEFAULT) {

    companion object {
        const val GENERATOR_KEY = "forms"
    }

    @Option
    @StringDefault("forms")
    @Description("The base directory in local resources to look for forms in.")
    lateinit var baseDir: String

    override fun startIndexing(context: OrchidContext): FormsModel {
        val forms = HashMap<String, Form>()
        getFormsByDatafiles(context, forms)

        return FormsModel(forms)
    }

    override fun startGeneration(context: OrchidContext, model: FormsModel) {

    }

    private fun getFormsByDatafiles(context: OrchidContext, forms: HashMap<String, Form>) {
        val formsPages = context.getResourceEntries(OrchidUtils.normalizePath(baseDir), context.parserExtensions.toTypedArray(), false)
        for (resource in formsPages) {
            resource.reference.isUsePrettyUrl = false
            val fileData = context.parse(resource.reference.extension, resource.content)
            val key = resource.reference.originalFileName
            val form = Form(context, key, fileData.toMap())
            forms.put(key, form)
        }
    }

}
