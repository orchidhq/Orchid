package com.eden.orchid.kotlindoc.resources

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.kotlindoc.model.KotlinClassdoc

class KotlinClassdocResource(
        context: OrchidContext,
        classDoc: KotlinClassdoc
) : BaseKotlindocResource(context, classDoc.qualifiedName, classDoc)