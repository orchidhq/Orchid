package com.eden.orchid.groovydoc.resources

import com.copperleaf.groovydoc.json.models.GroovydocPackageDoc
import com.eden.orchid.api.OrchidContext

class PackageDocResource(
        context: OrchidContext,
        packageDoc: GroovydocPackageDoc
) : BaseGroovydocResource(context, packageDoc.name, packageDoc)
