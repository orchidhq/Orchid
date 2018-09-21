package com.eden.orchid.search

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.search.components.FormComponent
import com.eden.orchid.search.model.FormField
import com.eden.orchid.search.model.fields.CheckboxField
import com.eden.orchid.search.model.fields.DropdownField
import com.eden.orchid.search.model.fields.HiddenField
import com.eden.orchid.search.model.fields.TextField
import com.eden.orchid.search.model.fields.TextareaField
import com.eden.orchid.utilities.addToSet

class FormsModule : OrchidModule() {

    override fun configure() {
        withResources(20)

        addToSet<OrchidGenerator, FormsGenerator>()
        addToSet<OrchidComponent, FormComponent>()
        addToSet<OptionExtractor<*>, FormOptionExtractor>()
        addToSet<FormField>(
                CheckboxField::class,
                DropdownField::class,
                HiddenField::class,
                TextareaField::class,
                TextField::class)
    }

}
