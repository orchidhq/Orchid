package com.eden.orchid.groovydoc.pages

import com.copperleaf.groovydoc.json.models.GroovydocClassDoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.groovydoc.GroovydocGenerator
import com.eden.orchid.groovydoc.models.GroovydocModel
import com.eden.orchid.groovydoc.resources.ClassDocResource

@Archetype(value = ConfigArchetype::class, key = "${GroovydocGenerator.GENERATOR_KEY}.classPages")
@Description(value = "Documentation for a Groovy class.", name = "Groovy class")
class GroovydocClassPage(
    context: OrchidContext,
    val classDoc: GroovydocClassDoc,
    val model: GroovydocModel
) : BaseGroovydocPage(ClassDocResource(context, classDoc), "groovydocClass", classDoc.name) {

    var packagePage: GroovydocPackagePage? = null

    override val itemIds: List<String>
        get() = listOf(
            classDoc.qualifiedName,
            classDoc.name
        )
}
