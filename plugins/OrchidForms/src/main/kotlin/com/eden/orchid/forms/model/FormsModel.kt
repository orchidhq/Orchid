package com.eden.orchid.forms.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormsModel
@Inject
constructor() {

    var forms: MutableMap<String, Form> = mutableMapOf()

    fun initialize(forms: MutableMap<String, Form>) {
        this.forms = forms
    }

}
