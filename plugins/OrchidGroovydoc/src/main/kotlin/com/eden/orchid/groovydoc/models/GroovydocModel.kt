package com.eden.orchid.groovydoc.models

import com.copperleaf.groovydoc.json.models.GroovydocConstructor
import com.copperleaf.groovydoc.json.models.GroovydocField
import com.copperleaf.groovydoc.json.models.GroovydocMethod
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.groovydoc.pages.GroovydocClassPage
import com.eden.orchid.groovydoc.pages.GroovydocPackagePage
import java.util.ArrayList

class GroovydocModel(
    val context: OrchidContext,
    val allClasses: List<GroovydocClassPage>,
    val allPackages: List<GroovydocPackagePage>
) : OrchidGenerator.Model {

    override val allPages: List<OrchidPage>
        get() {
            val pages = ArrayList<OrchidPage>()
            pages.addAll(allClasses)
            pages.addAll(allPackages)

            return pages
        }

    fun idFor(doc: GroovydocField): String {
        return "field__${doc.simpleSignature.replace("[^\\w.]".toRegex(), "_")}"
    }

    fun idFor(doc: GroovydocConstructor): String {
        return "constructor__${doc.simpleSignature.replace("[^\\w.]".toRegex(), "_")}"
    }

    fun idFor(doc: GroovydocMethod): String {
        return "method__${doc.simpleSignature.replace("[^\\w.]".toRegex(), "_")}"
    }

}
