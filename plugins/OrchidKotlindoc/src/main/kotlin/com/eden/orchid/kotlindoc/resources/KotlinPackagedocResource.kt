package com.eden.orchid.kotlindoc.resources

import com.copperleaf.dokka.json.models.KotlinPackageDoc
import com.eden.common.json.JSONElement
import com.eden.orchid.api.OrchidContext
import org.json.JSONObject

class KotlinPackagedocResource(
        context: OrchidContext,
        val packageDoc: KotlinPackageDoc
) : BaseKotlindocResource(context, packageDoc.qualifiedName, packageDoc) {

    override fun loadContent() {
        if (rawContent == null) {
            rawContent = doc.comment

            rawContent += packageDoc.methods.map { "${it.name}\n${it.comment}" }.joinToString("\n")
            rawContent += "\n"

            content = rawContent
            this.embeddedData = JSONElement(JSONObject())
        }
    }

}
