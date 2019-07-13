package com.eden.orchid.impl.compilers.clog

import com.caseyjbrooks.clog.parseltongue.Incantation
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.google.inject.Provider

class ClogIncantationWrapper(
    private val contextProvider: Provider<OrchidContext>,
    private val name: String,
    private val params: List<String>,
    private val functionClass: Class<out TemplateFunction>
) : Incantation {

    override fun call(input: Any?, vararg reagents: Any): Any {
        val freshFunction = contextProvider.get().resolve(functionClass)

        val functionOptionsList: List<Pair<String, Any?>> = params.zip(listOf(input, *reagents))
        val functionOptionsMap = mapOf(*functionOptionsList.toTypedArray())

        freshFunction.extractOptions(contextProvider.get(), functionOptionsMap)

        return freshFunction.apply(contextProvider.get(), null)
    }

    override fun getName(): String {
        return this.name
    }
}
