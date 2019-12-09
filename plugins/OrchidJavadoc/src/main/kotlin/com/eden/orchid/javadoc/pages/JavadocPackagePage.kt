package com.eden.orchid.javadoc.pages

import com.copperleaf.javadoc.json.models.JavaPackageDoc
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.javadoc.JavadocGenerator
import com.eden.orchid.javadoc.resources.PackageDocResource

@Archetype(value = ConfigArchetype::class, key = "${JavadocGenerator.GENERATOR_KEY}.packagePages")
@Description(value = "Documentation for a Java package.", name = "Java Package")
class JavadocPackagePage(
        context: OrchidContext,
        val packageDoc: JavaPackageDoc,
        val classes: List<JavadocClassPage>
) : BaseJavadocPage(PackageDocResource(context, packageDoc), "javadocPackage", packageDoc.name) {

    val innerPackages: MutableList<JavadocPackagePage> = ArrayList()

    override val itemIds: List<String>
        get() = listOf(packageDoc.qualifiedName)

    fun hasInterfaces(): Boolean {
        return classes.any { it.classDoc.kind == "interface" }
    }

    val interfaces: List<JavadocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "interface" }
        }

    fun hasAnnotations(): Boolean {
        return classes.any { it.classDoc.kind == "@interface" }
    }

    val annotations: List<JavadocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "@interface" }
        }

    fun hasEnums(): Boolean {
        return classes.any { it.classDoc.kind == "enum" }
    }

    val enums: List<JavadocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "enum" }
        }

    fun hasExceptions(): Boolean {
        return classes.any { it.classDoc.kind == "exception" }
    }

    val exceptions: List<JavadocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "exception" }
        }

    fun hasOrdinaryClasses(): Boolean {
        return classes.any { it.classDoc.kind == "class" }
    }

    val ordinaryClasses: List<JavadocClassPage>
        get() {
            return classes.filter { it.classDoc.kind == "class" }
        }

}
