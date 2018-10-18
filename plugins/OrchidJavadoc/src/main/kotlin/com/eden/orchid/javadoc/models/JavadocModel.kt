package com.eden.orchid.javadoc.models

import com.copperleaf.javadoc.json.models.JavaConstructor
import com.copperleaf.javadoc.json.models.JavaField
import com.copperleaf.javadoc.json.models.JavaMethod
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.pages.JavadocClassPage
import com.eden.orchid.javadoc.pages.JavadocPackagePage
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JavadocModel
@Inject
constructor(
        val context: OrchidContext
) {

    var allClasses: MutableList<JavadocClassPage> = ArrayList()
    var allPackages: MutableList<JavadocPackagePage> = ArrayList()

    private var classPageCache: MutableMap<String, OrchidPage?> = HashMap()
    private var packagePageCache: MutableMap<String, OrchidPage?> = HashMap()

    val allPages: List<OrchidPage>
        get() {
            val pages = ArrayList<OrchidPage>()
            pages.addAll(allClasses)
            pages.addAll(allPackages)

            return pages
        }

    fun initialize(allClasses: MutableList<JavadocClassPage>, allPackages: MutableList<JavadocPackagePage>) {
        this.allClasses = allClasses
        this.allPackages = allPackages
        this.classPageCache = HashMap()
        this.packagePageCache = HashMap()
    }

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
