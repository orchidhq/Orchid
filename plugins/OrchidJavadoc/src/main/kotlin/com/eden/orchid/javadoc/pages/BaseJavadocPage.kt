package com.eden.orchid.javadoc.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.JavadocGenerator
import com.eden.orchid.javadoc.resources.BaseJavadocResource
import com.sun.javadoc.Doc
import com.sun.javadoc.ProgramElementDoc
import com.sun.javadoc.Type

@Archetype(value = ConfigArchetype::class, key = "${JavadocGenerator.GENERATOR_KEY}.pages")
abstract class BaseJavadocPage(
        resource: BaseJavadocResource,
        key: String,
        title: String
) : OrchidPage(resource, key, title) {

    fun visibilityName(input: Any?): Any {
        if (input is ProgramElementDoc) {
            if (input.isPublic) {
                return "public"
            }
            else if (input.isProtected) {
                return "protected"
            }
            else if (input.isPrivate) {
                return "private"
            }
            else if (input.isPackagePrivate) {
                return ""
            }
        }

        throw IllegalArgumentException("Input to visibilityName must be a ProgramElementDoc, such as a field or method.")
    }

    fun isClassType(type: Any): Boolean {
        return type is Type
    }

    fun since(el: Doc): String {
        return el.tags("since").firstOrNull()?.text() ?: ""
    }

}
