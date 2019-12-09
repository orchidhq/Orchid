package com.eden.orchid.groovydoc.pages

import com.copperleaf.groovydoc.json.models.GroovydocPackageDoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.groovydoc.GroovydocGenerator
import com.eden.orchid.groovydoc.resources.PackageDocResource

@Archetype(value = ConfigArchetype::class, key = "${GroovydocGenerator.GENERATOR_KEY}.packagePages")
@Description(value = "Documentation for a Groovy package.", name = "Groovy Package")
class GroovydocPackagePage(
        context: OrchidContext,
        val packageDoc: GroovydocPackageDoc,
        val classes: List<GroovydocClassPage>
) : BaseGroovydocPage(PackageDocResource(context, packageDoc), "groovydocPackage", packageDoc.name) {

    val innerPackages: MutableList<GroovydocPackagePage> = ArrayList()

    override val itemIds: List<String>
        get() = listOf(packageDoc.qualifiedName)

    fun hasInterfaces(): Boolean {
        return classes.any { it.classDoc.kind == "interface" }
    }

    val interfaces: List<GroovydocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "interface" }
        }

    fun hasTraits(): Boolean {
        return classes.any { it.classDoc.kind == "trait" }
    }

    val traits: List<GroovydocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "trait" }
        }

    fun hasAnnotations(): Boolean {
        return classes.any { it.classDoc.kind == "@interface" }
    }

    val annotations: List<GroovydocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "@interface" }
        }

    fun hasEnums(): Boolean {
        return classes.any { it.classDoc.kind == "enum" }
    }

    val enums: List<GroovydocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "enum" }
        }

    fun hasExceptions(): Boolean {
        return classes.any { it.classDoc.kind == "exception" }
    }

    val exceptions: List<GroovydocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "exception" }
        }

    fun hasOrdinaryClasses(): Boolean {
        return classes.any { it.classDoc.kind == "class" }
    }

    val ordinaryClasses: List<GroovydocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "class" }
        }

}
