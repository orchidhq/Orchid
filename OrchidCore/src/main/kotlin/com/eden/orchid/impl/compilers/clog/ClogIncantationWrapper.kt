package com.eden.orchid.impl.compilers.clog

import com.caseyjbrooks.clog.parseltongue.Incantation
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.google.inject.Provider

import java.util.HashMap

class ClogIncantationWrapper(
    val contextProvider: Provider<OrchidContext>,
    private val name: String,
    params: List<String>,
    val functionClass: Class<out TemplateFunction>
) : Incantation {
    val params: List<String>

    init {
        if (params.isNotEmpty()) {
            this.params = params.subList(1, params.size)
        } else {
            this.params = params
        }
    }

    override fun call(input: Any, vararg reagents: Any): Any {
        val freshFunction = contextProvider.get().injector.getInstance(functionClass)

        val length = if (!EdenUtils.isEmpty(reagents) && !EdenUtils.isEmpty(params))
            Math.min(reagents.size, params.size)
        else
            0

        val `object` = HashMap<String, Any>()
        `object`[params[0]] = input
        for (i in 1 until length) {
            `object`[params[i]] = reagents[i - 1]
        }

        freshFunction.extractOptions(contextProvider.get(), `object`)

        return freshFunction.apply()
    }

    override fun getName(): String {
        return this.name
    }
}
