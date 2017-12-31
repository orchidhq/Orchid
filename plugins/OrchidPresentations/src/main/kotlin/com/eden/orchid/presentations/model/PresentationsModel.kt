package com.eden.orchid.presentations.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresentationsModel
@Inject constructor() {

    var presentations: MutableMap<String, Presentation> = mutableMapOf()

    fun initialize(presentations: MutableMap<String, Presentation>) {
        this.presentations = presentations
    }

}
