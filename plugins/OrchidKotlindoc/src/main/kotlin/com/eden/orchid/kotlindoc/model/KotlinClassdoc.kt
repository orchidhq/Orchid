package com.eden.orchid.kotlindoc.model

class KotlinClassdoc(
        name: String,
        comment: String,
        qualifiedName: String,
        val containingPackage: KotlinPackagedoc,
        val classType: String,
        val classlike: Boolean
) : KotlindocElement(name, comment, qualifiedName)