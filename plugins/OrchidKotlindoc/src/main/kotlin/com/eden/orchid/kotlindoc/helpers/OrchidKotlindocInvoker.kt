package com.eden.orchid.kotlindoc.helpers

import com.copperleaf.dokka.json.models.KotlinRootdoc
import com.eden.orchid.api.options.OptionsHolder
import com.google.inject.ImplementedBy

@ImplementedBy(OrchidKotlindocInvokerImpl::class)
interface OrchidKotlindocInvoker : OptionsHolder {

    fun getRootDoc(
        sourceDirs: List<String>,
        extraArgs: List<String>
    ): KotlinRootdoc?

}