package com.eden.orchid.kotlindoc.resources

import com.copperleaf.dokka.json.models.KotlinClassDoc
import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.utilities.asInputStream
import org.json.JSONObject
import java.io.InputStream

class KotlinClassdocResource(
        context: OrchidContext,
        val classDoc: KotlinClassDoc
) : BaseKotlindocResource(context, classDoc.qualifiedName, classDoc) {

    override fun getContentStream(): InputStream {
        var rawContent = doc.comment

        rawContent += classDoc.constructors.map { "${it.name}\n${it.comment}" }.joinToString("\n")
        rawContent += "\n"
        rawContent += classDoc.fields.map { "${it.name}\n${it.comment}" }.joinToString("\n")
        rawContent += "\n"
        rawContent += classDoc.methods.map { "${it.name}\n${it.comment}" }.joinToString("\n")
        rawContent += "\n"
        rawContent += classDoc.extensions.map { "${it.name}\n${it.comment}" }.joinToString("\n")
        rawContent += "\n"

        return rawContent.asInputStream()
    }
}
