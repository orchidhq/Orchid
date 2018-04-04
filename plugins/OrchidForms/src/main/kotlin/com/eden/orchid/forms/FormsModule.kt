package com.eden.orchid.forms

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.forms.components.FormComponent
import com.eden.orchid.forms.model.FormField
import com.eden.orchid.forms.model.fields.DropdownField
import com.eden.orchid.forms.model.fields.HiddenField
import com.eden.orchid.forms.model.fields.TextField
import com.eden.orchid.forms.model.fields.TextareaField

class FormsModule : OrchidModule() {

    override fun configure() {
        addToSet(PluginResourceSource::class.java,
                FormsResourceSource::class.java)

        addToSet(OrchidGenerator::class.java,
                FormsGenerator::class.java)

        addToSet(OrchidComponent::class.java,
                FormComponent::class.java)

        addToSet(OptionExtractor::class.java,
                FormOptionExtractor::class.java,
                FormFieldListOptionExtractor::class.java)

        addToSet(FormField::class.java,
                TextField::class.java,
                TextareaField::class.java,
                DropdownField::class.java,
                HiddenField::class.java)
    }

}
