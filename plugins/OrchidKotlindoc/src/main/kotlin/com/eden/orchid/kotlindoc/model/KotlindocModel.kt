package com.eden.orchid.kotlindoc.model

import com.copperleaf.dokka.json.models.KotlinConstructor
import com.copperleaf.dokka.json.models.KotlinField
import com.copperleaf.dokka.json.models.KotlinMethod
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.kotlindoc.page.KotlindocClassPage
import com.eden.orchid.kotlindoc.page.KotlindocPackagePage

class KotlindocModel
constructor(
    val context: OrchidContext,
    var allClasses: List<KotlindocClassPage>,
    var allPackages: List<KotlindocPackagePage>
) : OrchidGenerator.Model {

    override val allPages: List<OrchidPage>
        get() = listOf(
            *allClasses.toTypedArray(),
            *allPackages.toTypedArray()
        )

    fun initialize(allClasses: MutableList<KotlindocClassPage>, allPackages: MutableList<KotlindocPackagePage>) {
        this.allClasses = allClasses
        this.allPackages = allPackages
    }

    fun idFor(doc: KotlinField): String {
        return "field__${doc.simpleSignature.replace("[^\\w.]".toRegex(), "_")}"
    }

    fun idFor(doc: KotlinConstructor): String {
        return "constructor__${doc.simpleSignature.replace("[^\\w.]".toRegex(), "_")}"
    }

    fun idFor(doc: KotlinMethod): String {
        return "method__${doc.simpleSignature.replace("[^\\w.]".toRegex(), "_")}"
    }

}
