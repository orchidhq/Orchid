package com.eden.orchid.kotlindoc.helpers

import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.kotlindoc.model.KotlinRootdoc
import com.google.inject.ImplementedBy

@ImplementedBy(KotlindocInvokerImpl::class)
interface KotlindocInvoker : OptionsHolder {

    fun getRootDoc(sourceDirs: List<String>): KotlinRootdoc?

}