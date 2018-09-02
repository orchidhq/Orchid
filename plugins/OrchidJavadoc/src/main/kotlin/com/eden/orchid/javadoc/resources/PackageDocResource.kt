package com.eden.orchid.javadoc.resources

import com.eden.orchid.api.OrchidContext
import com.sun.javadoc.PackageDoc

class PackageDocResource(
        context: OrchidContext,
        packageDoc: PackageDoc
) : BaseJavadocResource(context, packageDoc.name(), packageDoc)
