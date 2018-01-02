package com.eden.orchid.forms

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.forms.model.Form
import com.eden.orchid.forms.model.FormsModel
import com.eden.orchid.utilities.OrchidUtils
import java.util.*
import java.util.stream.Stream
import javax.inject.Inject

@Description("Indexes form definitions so they can be easily referenced from components on different pages.")
class FormsGenerator @Inject
constructor(context: OrchidContext, private val model: FormsModel) : OrchidGenerator(context, "forms", 20) {

    @Option("baseDir") @StringDefault("forms")
    lateinit var formsBaseDir: String

    override fun startIndexing(): List<OrchidPage>? {
        val forms = HashMap<String, Form>()

        val formsData = context.getDatafiles(OrchidUtils.normalizePath(formsBaseDir))

        for (key in formsData.keySet()) {
            forms.put(key, Form(context, key, formsData.getJSONObject(key)))
        }

        model.initialize(forms)

        return null
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {

    }

}
