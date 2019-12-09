package com.eden.orchid.javadoc.pages

import com.copperleaf.javadoc.json.models.JavaClassDoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.javadoc.JavadocGenerator
import com.eden.orchid.javadoc.models.JavadocModel
import com.eden.orchid.javadoc.resources.ClassDocResource

@Archetype(value = ConfigArchetype::class, key = "${JavadocGenerator.GENERATOR_KEY}.classPages")
@Description(value = "Documentation for a Java class.", name = "Java class")
class JavadocClassPage(
        context: OrchidContext,
        val classDoc: JavaClassDoc
) : BaseJavadocPage(ClassDocResource(context, classDoc), "javadocClass", classDoc.name) {

    lateinit var model: JavadocModel
    var packagePage: JavadocPackagePage? = null

    override val itemIds: List<String>
        get() = listOf(
            classDoc.qualifiedName,
            classDoc.name
        )
}
