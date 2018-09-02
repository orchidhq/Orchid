package com.eden.orchid.javadoc.resources

import com.eden.orchid.api.OrchidContext
import com.sun.javadoc.ClassDoc

class ClassDocResource(
        context: OrchidContext,
        classDoc: ClassDoc
) : BaseJavadocResource(context, classDoc.qualifiedName(), classDoc)
