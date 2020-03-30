package com.eden.orchid.api.server.annotations

import com.eden.orchid.api.options.OptionsHolder
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class Put(
    val path: String,
    val params: KClass<out OptionsHolder> = OptionsHolder::class
)
