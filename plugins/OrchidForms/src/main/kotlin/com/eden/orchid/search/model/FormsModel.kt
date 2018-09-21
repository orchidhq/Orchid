package com.eden.orchid.search.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormsModel @Inject
constructor() {

    lateinit var forms: MutableMap<String, Form>

    fun initialize(forms: MutableMap<String, Form>) {
        this.forms = forms
    }

}
