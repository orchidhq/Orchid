package com.eden.orchid.javadoc.resources;

import com.eden.orchid.api.OrchidContext;
import com.sun.javadoc.PackageDoc;

public class PackageDocResource extends BaseJavadocResource {

    public PackageDocResource(OrchidContext context, PackageDoc packageDoc) {
        super(context, packageDoc.name(), packageDoc);
    }
}
