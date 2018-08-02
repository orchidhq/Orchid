package com.eden.orchid.javadoc.helpers

import com.eden.orchid.api.options.OptionsHolder
import com.google.inject.ImplementedBy
import com.sun.javadoc.RootDoc

@ImplementedBy(JavadocInvokerImpl::class)
interface JavadocInvoker : OptionsHolder {

    fun getRootDoc(sourceDirs: List<String>): RootDoc?

}