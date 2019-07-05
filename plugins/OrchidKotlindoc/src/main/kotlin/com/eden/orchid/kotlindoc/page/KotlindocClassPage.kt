package com.eden.orchid.kotlindoc.page

import com.copperleaf.dokka.json.models.KotlinClassDoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.kotlindoc.KotlindocGenerator
import com.eden.orchid.kotlindoc.model.KotlindocModel
import com.eden.orchid.kotlindoc.resources.KotlinClassdocResource

@Archetype(value = ConfigArchetype::class, key = "${KotlindocGenerator.GENERATOR_KEY}.classPages")
@Description(value = "Documentation for a Kotlin or Java class.", name = "Kotlin class")
class KotlindocClassPage(
        context: OrchidContext,
        val classDoc: KotlinClassDoc,
        val model: KotlindocModel
) : BaseKotlindocPage(KotlinClassdocResource(context, classDoc), "kotlindocClass", classDoc.name) {

    var packagePage: KotlindocPackagePage? = null

    override val itemIds: List<String>
        get() = listOf(
            classDoc.qualifiedName,
            classDoc.name
        )
}
