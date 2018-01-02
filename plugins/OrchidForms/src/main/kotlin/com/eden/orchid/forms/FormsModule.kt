package com.eden.orchid.forms

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.forms.components.FormComponent
import com.eden.orchid.forms.model.FormField
import com.eden.orchid.forms.model.fields.DropdownField
import com.eden.orchid.forms.model.fields.TextField

class FormsModule : OrchidModule() {

    override fun configure() {
        addToSet(PluginResourceSource::class.java,
                FormsResourceSource::class.java)

        addToSet(OrchidGenerator::class.java,
                FormsGenerator::class.java)

        addToSet(OrchidComponent::class.java,
                FormComponent::class.java)

        addToSet(OptionExtractor::class.java,
                FormOptionExtractor::class.java)

        addToSet(FormField::class.java,
                TextField::class.java,
                DropdownField::class.java)
    }

}
