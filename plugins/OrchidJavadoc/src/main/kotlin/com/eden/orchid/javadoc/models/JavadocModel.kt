package com.eden.orchid.javadoc.models

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.pages.JavadocClassPage
import com.eden.orchid.javadoc.pages.JavadocPackagePage
import com.sun.javadoc.ConstructorDoc
import com.sun.javadoc.FieldDoc
import com.sun.javadoc.MethodDoc
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

    fun idFor(doc: FieldDoc): String {
        return "field__${doc.name()}"
    }

    fun idFor(doc: ConstructorDoc): String {
        val paramNames = doc
                .parameters()
                .map { it.typeName().replace('.', '-').replace("\\[.*?]".toRegex(), "").replace("<.*?>".toRegex(), "") }
                .joinToString(separator = "_")

        return "constructor__$paramNames"
    }

    fun idFor(doc: MethodDoc): String {
        val methodName = doc.name()
        val paramNames = doc
                .parameters()
                .map { it.typeName().replace('.', '-').replace("\\[.*?]".toRegex(), "").replace("<.*?>".toRegex(), "") }
                .joinToString(separator = "_")

        return "method__${methodName}__$paramNames"
    }

    fun nameFor(doc: FieldDoc): String {
        return doc.name()
    }

    fun nameFor(doc: ConstructorDoc): String {
        return doc
                .parameters()
                .map { it.type().simpleTypeName() }
                .joinToString(separator = ", ")
    }

    fun nameFor(doc: MethodDoc): String {
        val methodName = doc.name()
        val paramNames = doc
                .parameters()
                .map { it.type().simpleTypeName() }
                .joinToString(separator = ", ")

        return "${methodName}($paramNames)"
    }

}
