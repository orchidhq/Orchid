package com.eden.orchid.javadoc.pages;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.javadoc.resources.PackageDocResource;
import com.sun.javadoc.PackageDoc;
import lombok.Getter;

import java.util.List;

@Getter
public class JavadocPackagePage extends OrchidPage {

    private PackageDoc packageDoc;
    private List<JavadocClassPage> classes;

    public JavadocPackagePage(OrchidContext context, PackageDoc packageDoc, List<JavadocClassPage> classes) {
        super(new PackageDocResource(context, packageDoc), "javadocPackage", packageDoc.name());
        this.packageDoc = packageDoc;
        this.classes = classes;
    }

}
