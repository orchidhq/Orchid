package com.eden.orchid.groovydoc.resources

import com.copperleaf.groovydoc.json.models.GroovydocClassDoc
import com.eden.orchid.api.OrchidContext

class ClassDocResource(
        context: OrchidContext,
        classDoc: GroovydocClassDoc
) : BaseGroovydocResource(context, classDoc.qualifiedName, classDoc)
