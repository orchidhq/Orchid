package com.eden.orchid.javadoc.pages;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.SimpleResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.sun.javadoc.ClassDoc;

public class JavadocClassPage extends OrchidPage {

    private JavadocPackagePage packagePage;
    private ClassDoc classDoc;

    public JavadocClassPage(OrchidContext context, ClassDoc classDoc) {
        super(new SimpleResource(context, classDoc.qualifiedName().replaceAll("\\.", "/") + ".html"),
                "javadocClass",
                classDoc.typeName());
        this.classDoc = classDoc;
    }

    public JavadocPackagePage getPackagePage() {
        return packagePage;
    }

    public ClassDoc getClassDoc() {
        return classDoc;
    }

    public void setPackagePage(JavadocPackagePage packagePage) {
        this.packagePage = packagePage;
    }

    public void setClassDoc(ClassDoc classDoc) {
        this.classDoc = classDoc;
    }
}
