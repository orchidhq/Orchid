package com.eden.orchid.javadoc.helpers

import com.copperleaf.javadoc.json.models.JavadocRootdoc
import com.eden.orchid.api.options.OptionsHolder
import com.google.inject.ImplementedBy

@ImplementedBy(OrchidJavadocInvokerImpl::class)
interface OrchidJavadocInvoker : OptionsHolder {

    fun getRootDoc(sourceDirs: List<String>): JavadocRootdoc?

}