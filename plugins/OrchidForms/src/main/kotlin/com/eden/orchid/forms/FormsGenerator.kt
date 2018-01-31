package com.eden.orchid.forms

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.forms.model.Form
import com.eden.orchid.forms.model.FormsModel
import com.eden.orchid.forms.model.fields.HiddenField
import com.eden.orchid.forms.pages.FormSubmissionPage
import com.eden.orchid.utilities.OrchidUtils
import org.json.JSONObject
import java.util.*
import java.util.stream.Stream
import javax.inject.Inject
import kotlin.collections.ArrayList

@Description("Indexes form definitions so they can be easily referenced from components on different pages.")
class FormsGenerator @Inject
constructor(context: OrchidContext, private val model: FormsModel) : OrchidGenerator(context, "forms", OrchidGenerator.PRIORITY_DEFAULT) {

    @Option @StringDefault("forms")
    @Description("The base directory to look for forms in.")
    lateinit var baseDir: String

    override fun startIndexing(): List<OrchidPage>? {
        val forms = HashMap<String, Form>()
        getFormsByDatafiles(forms)

        val pages = getFormsWithSubmissionPages(forms)

        model.initialize(forms)

        return pages
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        pages.forEach { context.renderTemplate(it) }
    }

    private fun getFormsByDatafiles(forms: HashMap<String, Form>) {
        val formsPages = context.getResourceEntries(OrchidUtils.normalizePath(baseDir), context.parserExtensions.toTypedArray(), false)
        for (resource in formsPages) {
            resource.reference.isUsePrettyUrl = false
            val fileData = context.parse(resource.reference.extension, resource.content)
            val key = resource.reference.originalFileName
            val form = Form(context, key, fileData)
            forms.put(key, form)
        }
    }

    private fun getFormsWithSubmissionPages(forms: HashMap<String, Form>): List<OrchidPage> {
        val pages = ArrayList<OrchidPage>()
        val formsPages = context.getResourceEntries(OrchidUtils.normalizePath(baseDir), context.compilerExtensions.toTypedArray(), false)

        if(!EdenUtils.isEmpty(formsPages)) {
            for (resource in formsPages) {
                val key = resource.reference.originalFileName

                val formSubmissionPage = FormSubmissionPage(resource, "form", null, null)
                formSubmissionPage.reference.stripFromPath(OrchidUtils.normalizePath(baseDir))
                formSubmissionPage.reference.path = OrchidUtils.normalizePath("submit/" + formSubmissionPage.reference.originalPath)
                pages.add(formSubmissionPage)

                val form = Form(context, key, formSubmissionPage.data.getJSONObject("form"))

                // if the form does not specify an action, set its page as the action.
                if(EdenUtils.isEmpty(form.action)) {
                    form.action = formSubmissionPage.reference.toString()
                }

                val onSubmitField = HiddenField(context)
                onSubmitField.initialize("__onSubmit", JSONObject())
                onSubmitField.value = formSubmissionPage.reference.toString()

                form.fields.put("__onSubmit", onSubmitField)

                forms.put(key, form)
            }
        }

        return pages
    }

}
