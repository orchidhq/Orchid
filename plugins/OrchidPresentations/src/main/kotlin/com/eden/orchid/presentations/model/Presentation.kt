package com.eden.orchid.presentations.model

data class Presentation(
        val key: String,
        val slides: List<Slide>,
        var title: String = ""
)
