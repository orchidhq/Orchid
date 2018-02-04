package com.eden.orchid.javadoc.pages

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.JavadocModel
import com.eden.orchid.javadoc.resources.ClassDocResource
import com.sun.javadoc.ClassDoc

class JavadocClassPage(context: OrchidContext, var classDoc: ClassDoc) : OrchidPage(ClassDocResource(context, classDoc), "javadocClass", classDoc.typeName()) {

    var packagePage: JavadocPackagePage? = null

    val classType: String
        get() {
            if (classDoc.isInterface) {
                return "interface"
            } else if (classDoc.isAnnotationType) {
                return "@interface"
            } else if (classDoc.isEnum) {
                return "enum"
            }

            return "class"
        }

    val summary: String
        get() {
            return context.compile("md", classDoc.firstSentenceTags().joinToString(" ", transform = {it.text()}))
        }

    fun getClassPage(className: String?): OrchidPage? {
        val javadocModel = context.injector.getInstance(JavadocModel::class.java)

        for (classPage in javadocModel.allClasses) {
            if (classPage.classDoc.qualifiedName() == className) {
                return classPage
            } else if (classPage.classDoc.name() == className) {
                return classPage
            }
        }

        return null
    }
}
