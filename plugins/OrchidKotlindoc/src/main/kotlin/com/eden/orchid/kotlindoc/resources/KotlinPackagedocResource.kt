package com.eden.orchid.kotlindoc.resources

import com.copperleaf.dokka.json.models.KotlinPackageDoc
import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.utilities.asInputStream
import org.json.JSONObject
import java.io.InputStream

class KotlinPackagedocResource(
    context: OrchidContext,
    val packageDoc: KotlinPackageDoc
) : BaseKotlindocResource(context, packageDoc.qualifiedName, packageDoc) {

    override fun getContentStream(): InputStream {
        var rawContent = doc.comment

        rawContent += packageDoc.methods.map { "${it.name}\n${it.comment}" }.joinToString("\n")
        rawContent += "\n"

        return rawContent.asInputStream()
    }

}
