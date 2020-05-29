package com.eden.orchid.api.site

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.AllOptions
import com.eden.orchid.api.options.annotations.Option
import com.google.inject.name.Named
import javax.inject.Inject

class DefaultBaseUrlFactory
@Inject
constructor(
    @Named("baseUrl") private val baseUrl: String
) : BaseUrlFactory("default", 10) {

    @AllOptions
    lateinit var allOptions: Map<String?, Any>

    @Option
    var value: String? = null

    override fun isEnabled(context: OrchidContext): Boolean = true
    override fun getBaseUrl(context: OrchidContext) : String = (value?.takeIf { it.isNotBlank() } ?: baseUrl)
}
