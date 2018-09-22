package com.eden.orchid.api.options.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class RelationConfig(val collectionType: String = "", val collectionId: String = "", val itemId: String = "")
