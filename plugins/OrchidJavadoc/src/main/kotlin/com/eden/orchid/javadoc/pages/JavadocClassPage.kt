package com.eden.orchid.javadoc.pages

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.javadoc.JavadocGenerator
import com.eden.orchid.javadoc.models.JavadocModel
import com.eden.orchid.javadoc.resources.ClassDocResource
import com.sun.javadoc.ClassDoc

@Archetype(value = ConfigArchetype::class, key = "${JavadocGenerator.GENERATOR_KEY}.classPages")
class JavadocClassPage(context: OrchidContext, val classDoc: ClassDoc, val model: JavadocModel)
    : BaseJavadocPage(ClassDocResource(context, classDoc), "javadocClass", classDoc.typeName()) {

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

}
