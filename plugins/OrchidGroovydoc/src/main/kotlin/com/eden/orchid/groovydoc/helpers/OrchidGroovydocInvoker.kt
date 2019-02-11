package com.eden.orchid.groovydoc.helpers

import com.copperleaf.groovydoc.json.models.GroovydocRootdoc
import com.eden.orchid.api.options.OptionsHolder
import com.google.inject.ImplementedBy

@ImplementedBy(OrchidGroovydocInvokerImpl::class)
interface OrchidGroovydocInvoker : OptionsHolder {

    fun getRootDoc(
        sourceDirs: List<String>,
        extraArgs: List<String>
    ): GroovydocRootdoc?

}