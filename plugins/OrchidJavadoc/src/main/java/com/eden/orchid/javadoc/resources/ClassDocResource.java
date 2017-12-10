package com.eden.orchid.javadoc.resources;

import com.eden.orchid.api.OrchidContext;
import com.sun.javadoc.ClassDoc;

public class ClassDocResource extends BaseJavadocResource {

    public ClassDocResource(OrchidContext context, ClassDoc classDoc) {
        super(context, classDoc.qualifiedName(), classDoc);
    }

}
