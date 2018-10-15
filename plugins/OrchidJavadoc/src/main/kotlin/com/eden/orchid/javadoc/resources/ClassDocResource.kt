package com.eden.orchid.javadoc.resources

import com.copperleaf.javadoc.json.models.JavaClassDoc
import com.eden.orchid.api.OrchidContext

class ClassDocResource(
        context: OrchidContext,
        classDoc: JavaClassDoc
) : BaseJavadocResource(context, classDoc.qualifiedName, classDoc)
