package com.eden.orchid.forms.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.forms.model.Form

import javax.inject.Inject

class FormComponent
@Inject constructor(context: OrchidContext)
    : OrchidComponent(context, "form", 20) {

    @Option
    var form: Form? = null

    override fun addAssets() {
        super.addAssets()
        addCss("assets/css/form.scss")
    }
}
