package com.eden.orchid.kotlindoc.page

import com.copperleaf.dokka.json.models.KotlinPackageDoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.kotlindoc.KotlindocGenerator
import com.eden.orchid.kotlindoc.model.KotlindocModel
import com.eden.orchid.kotlindoc.resources.KotlinPackagedocResource

@Archetype(value = ConfigArchetype::class, key = "${KotlindocGenerator.GENERATOR_KEY}.packagePages")
@Description(value = "Documentation for a Kotlin or Java package.", name = "Kotlin Package")
class KotlindocPackagePage(
        context: OrchidContext,
        val packageDoc: KotlinPackageDoc,
        val classes: List<KotlindocClassPage>,
        val model: KotlindocModel
) : BaseKotlindocPage(KotlinPackagedocResource(context, packageDoc), "kotlindocPackage", packageDoc.qualifiedName) {

    val innerPackages: MutableList<KotlindocPackagePage> = ArrayList()

    override val itemIds: List<String>
        get() = listOf(packageDoc.qualifiedName)

    fun hasInterfaces(): Boolean {
        return classes.any { it.classDoc.kind == "Interface" }
    }

    val interfaces: List<KotlindocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "Interface" }
        }

    fun hasOrdinaryClasses(): Boolean {
        return classes.any { it.classDoc.kind == "Class" }
    }

    val ordinaryClasses: List<KotlindocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "Class" }
        }

    fun hasEnums(): Boolean {
        return classes.any { it.classDoc.kind == "Enum" }
    }

    val enums: List<KotlindocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "Enum" }
        }

    fun hasExceptions(): Boolean {
        return classes.any { it.classDoc.kind == "Exception" }
    }

    val exceptions: List<KotlindocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "Exception" }
        }

    fun hasAnnotations(): Boolean {
        return classes.any { it.classDoc.kind == "AnnotationClass" }
    }

    val annotations: List<KotlindocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "AnnotationClass" }
        }

}