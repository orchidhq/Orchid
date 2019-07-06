package com.eden.orchid.javadoc.models

import com.copperleaf.javadoc.json.models.JavaConstructor
import com.copperleaf.javadoc.json.models.JavaField
import com.copperleaf.javadoc.json.models.JavaMethod
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.pages.JavadocClassPage
import com.eden.orchid.javadoc.pages.JavadocPackagePage

class JavadocModel(
    val context: OrchidContext,
    val allClasses: List<JavadocClassPage>,
    val allPackages: List<JavadocPackagePage>
) : OrchidGenerator.Model {

    override val allPages: List<OrchidPage>
        get() = listOf(
            *allClasses.toTypedArray(),
            *allPackages.toTypedArray()
        )

    fun idFor(doc: JavaField): String {
        return "field__${doc.simpleSignature.replace("[^\\w.]".toRegex(), "_")}"
    }

    fun idFor(doc: JavaConstructor): String {
        return "constructor__${doc.simpleSignature.replace("[^\\w.]".toRegex(), "_")}"
    }

    fun idFor(doc: JavaMethod): String {
        return "method__${doc.simpleSignature.replace("[^\\w.]".toRegex(), "_")}"
    }

}
