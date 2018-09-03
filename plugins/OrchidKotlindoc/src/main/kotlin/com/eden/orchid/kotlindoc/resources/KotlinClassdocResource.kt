package com.eden.orchid.kotlindoc.resources

import com.copperleaf.dokka.json.models.KotlinClassDoc
import com.eden.orchid.api.OrchidContext

class KotlinClassdocResource(
        context: OrchidContext,
        classDoc: KotlinClassDoc
) : BaseKotlindocResource(context, classDoc.qualifiedName, classDoc)