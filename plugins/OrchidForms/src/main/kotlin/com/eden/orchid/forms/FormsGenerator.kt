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
class FormsGenerator : OrchidGenerator<FormsModel>(GENERATOR_KEY, Stage.CONTENT) {

    companion object {
        const val GENERATOR_KEY = "forms"
    }

    @Option
    @StringDefault("forms")
    @Description("The base directory in local resources to look for forms in.")
    lateinit var baseDir: String

    override fun startIndexing(context: OrchidContext): FormsModel {
        val forms = getFormsByDatafiles(context)

        return FormsModel(forms)
    }

    private fun getFormsByDatafiles(context: OrchidContext) : List<Form> {
        return context
            .getResourceEntries(
                OrchidUtils.normalizePath(baseDir),
                context.parserExtensions.toTypedArray(),
                false,
                null
            )
            .map { resource ->
                resource.reference.isUsePrettyUrl = false
                val fileData = context.parse(resource.reference.extension, resource.content)
                val key = resource.reference.originalFileName
                Form(context, key, fileData)
            }
    }

}
