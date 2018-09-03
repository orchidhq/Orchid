package com.eden.orchid.kotlindoc.resources

import com.copperleaf.dokka.json.models.KotlinPackageDoc
import com.eden.orchid.api.OrchidContext

class KotlinPackagedocResource(
        context: OrchidContext,
        packageDoc: KotlinPackageDoc
) : BaseKotlindocResource(context, packageDoc.qualifiedName, packageDoc)
