package com.eden.orchid.javadoc.pages;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.javadoc.JavadocModel;
import com.eden.orchid.javadoc.resources.ClassDocResource;
import com.sun.javadoc.ClassDoc;

public class JavadocClassPage extends OrchidPage {

    private JavadocPackagePage packagePage;
    private ClassDoc classDoc;

    @Option
    public String deprecated;

    public JavadocClassPage(OrchidContext context, ClassDoc classDoc) {
        super(new ClassDocResource(context, classDoc), "javadocClass", classDoc.typeName());
        this.classDoc = classDoc;
    }

    public String getClassType() {
        if(this.classDoc.isInterface()) {
            return "interface";
        }
        else if(this.classDoc.isAnnotationType()) {
            return "@interface";
        }
        else if(this.classDoc.isEnum()) {
            return "enum";
        }

        return "class";
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

    public OrchidPage getClassPage(String className) {
        JavadocModel javadocModel = context.getInjector().getInstance(JavadocModel.class);

        for(JavadocClassPage classPage : javadocModel.getAllClasses()) {
            if(classPage.classDoc.qualifiedName().equals(className)) {
                return classPage;
            }
            else if(classPage.classDoc.name().equals(className)) {
                return classPage;
            }
        }

        return null;
    }
}
