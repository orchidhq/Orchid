package com.eden.orchid.kotlindoc.resources

import com.copperleaf.dokka.json.models.KotlinClassDoc
import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import org.json.JSONObject

class KotlinClassdocResource(
        context: OrchidContext,
        val classDoc: KotlinClassDoc
) : BaseKotlindocResource(context, classDoc.qualifiedName, classDoc) {

    override fun loadContent() {
        if (rawContent == null) {
            rawContent = doc.comment

            rawContent += classDoc.constructors.map { "${it.name}\n${it.comment}" }.joinToString("\n")
            rawContent += "\n"
            rawContent += classDoc.fields.map { "${it.name}\n${it.comment}" }.joinToString("\n")
            rawContent += "\n"
            rawContent += classDoc.methods.map { "${it.name}\n${it.comment}" }.joinToString("\n")
            rawContent += "\n"
            rawContent += classDoc.extensions.map { "${it.name}\n${it.comment}" }.joinToString("\n")
            rawContent += "\n"

            content = rawContent
            this.embeddedData = JSONElement(JSONObject())
        }
    }

}