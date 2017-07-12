package com.eden.orchid.javadoc.pages;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.SimpleResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.sun.javadoc.PackageDoc;

import java.util.List;

public class JavadocPackagePage extends OrchidPage {

    private PackageDoc packageDoc;
    List<JavadocClassPage> classes;

    public JavadocPackagePage(OrchidContext context, PackageDoc packageDoc, List<JavadocClassPage> classes) {
        super(new SimpleResource(context, packageDoc.name().replaceAll("\\.", "/") + ".html"));
        this.type = "javadocPackage";
        this.packageDoc = packageDoc;
        this.getReference().setTitle(packageDoc.name());
        this.classes = classes;
    }

    public PackageDoc getPackageDoc() {
        return packageDoc;
    }

    public List<JavadocClassPage> getClasses() {
        return classes;
    }

    public void setPackageDoc(PackageDoc packageDoc) {
        this.packageDoc = packageDoc;
    }

    public void setClasses(List<JavadocClassPage> classes) {
        this.classes = classes;
    }
}
