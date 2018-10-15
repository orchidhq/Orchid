package com.eden.orchid.javadoc.resources

import com.copperleaf.javadoc.json.models.JavaPackageDoc
import com.eden.orchid.api.OrchidContext

class PackageDocResource(
        context: OrchidContext,
        packageDoc: JavaPackageDoc
) : BaseJavadocResource(context, packageDoc.name, packageDoc)
