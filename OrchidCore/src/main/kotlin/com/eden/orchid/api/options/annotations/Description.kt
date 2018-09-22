package com.eden.orchid.api.options.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(
        AnnotationTarget.FIELD,
        AnnotationTarget.CLASS,
        AnnotationTarget.FILE,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.CONSTRUCTOR
)
annotation class Description(val value: String, val name: String = "")
