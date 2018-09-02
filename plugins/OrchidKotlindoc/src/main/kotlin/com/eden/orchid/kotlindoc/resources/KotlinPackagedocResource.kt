package com.eden.orchid.kotlindoc.resources

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.kotlindoc.model.KotlinPackagedoc

class KotlinPackagedocResource(
        context: OrchidContext,
        packageDoc: KotlinPackagedoc
) : BaseKotlindocResource(context, packageDoc.qualifiedName, packageDoc)
