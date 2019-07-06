package com.eden.orchid.forms

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.forms.components.FormComponent
import com.eden.orchid.forms.model.FormField
import com.eden.orchid.forms.model.fields.CheckboxField
import com.eden.orchid.forms.model.fields.DropdownField
import com.eden.orchid.forms.model.fields.HiddenField
import com.eden.orchid.forms.model.fields.RadioButtonsField
import com.eden.orchid.forms.model.fields.TextField
import com.eden.orchid.forms.model.fields.TextareaField
import com.eden.orchid.utilities.addToSet

class FormsModule : OrchidModule() {

    override fun configure() {
        withResources(20)

        addToSet<OrchidGenerator<*>, FormsGenerator>()
        addToSet<OrchidComponent, FormComponent>()
        addToSet<OptionExtractor<*>, FormOptionExtractor>()
        addToSet<FormField>(
                CheckboxField::class,
                RadioButtonsField::class,
                DropdownField::class,
                HiddenField::class,
                TextareaField::class,
                TextField::class)
    }

}
