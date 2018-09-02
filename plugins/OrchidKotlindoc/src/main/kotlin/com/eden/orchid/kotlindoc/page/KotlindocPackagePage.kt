package com.eden.orchid.kotlindoc.page

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.kotlindoc.KotlindocGenerator
import com.eden.orchid.kotlindoc.model.KotlindocModel
import com.eden.orchid.kotlindoc.model.KotlinPackagedoc
import com.eden.orchid.kotlindoc.resources.KotlinPackagedocResource

@Archetype(value = ConfigArchetype::class, key = "${KotlindocGenerator.GENERATOR_KEY}.packagePages")
class KotlindocPackagePage(
        context: OrchidContext,
        val packageDoc: KotlinPackagedoc,
        val classes: List<KotlindocClassPage>,
        val model: KotlindocModel
) : BaseKotlindocPage(KotlinPackagedocResource(context, packageDoc), "kotlindocPackage", packageDoc.qualifiedName) {

    val innerPackages: MutableList<KotlindocPackagePage> = ArrayList()

    fun hasInterfaces(): Boolean {
        return classes.any { it.classDoc.classType == "Interface" }
    }

    val interfaces: List<KotlindocClassPage>
        get() {
            return classes.filter { it.classDoc.classType == "Interface" }
        }

    fun hasOrdinaryClasses(): Boolean {
        return classes.any { it.classDoc.classType == "Class" }
    }

    val ordinaryClasses: List<KotlindocClassPage>
        get() {
            return classes.filter { it.classDoc.classType == "Class" }
        }

    fun hasEnums(): Boolean {
        return classes.any { it.classDoc.classType == "Enum" }
    }

    val enums: List<KotlindocClassPage>
        get() {
            return classes.filter { it.classDoc.classType == "Enum" }
        }

    fun hasExceptions(): Boolean {
        return classes.any { it.classDoc.classType == "Exception" }
    }

    val exceptions: List<KotlindocClassPage>
        get() {
            return classes.filter { it.classDoc.classType == "Exception" }
        }

    fun hasAnnotations(): Boolean {
        return classes.any { it.classDoc.classType == "AnnotationClass" }
    }

    val annotations: List<KotlindocClassPage>
        get() {
            return classes.filter { it.classDoc.classType == "AnnotationClass" }
        }

}