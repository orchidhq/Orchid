package com.eden.orchid.groovydoc.models

import com.copperleaf.groovydoc.json.models.GroovydocConstructor
import com.copperleaf.groovydoc.json.models.GroovydocField
import com.copperleaf.groovydoc.json.models.GroovydocMethod
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.groovydoc.pages.GroovydocClassPage
import com.eden.orchid.groovydoc.pages.GroovydocPackagePage
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroovydocModel
@Inject
constructor(
        val context: OrchidContext
) {

    var allClasses: MutableList<GroovydocClassPage> = ArrayList()
    var allPackages: MutableList<GroovydocPackagePage> = ArrayList()

    private var classPageCache: MutableMap<String, OrchidPage?> = HashMap()
    private var packagePageCache: MutableMap<String, OrchidPage?> = HashMap()

    val allPages: List<OrchidPage>
        get() {
            val pages = ArrayList<OrchidPage>()
            pages.addAll(allClasses)
            pages.addAll(allPackages)

            return pages
        }

    fun initialize(allClasses: MutableList<GroovydocClassPage>, allPackages: MutableList<GroovydocPackagePage>) {
        this.allClasses = allClasses
        this.allPackages = allPackages
        this.classPageCache = HashMap()
        this.packagePageCache = HashMap()
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
