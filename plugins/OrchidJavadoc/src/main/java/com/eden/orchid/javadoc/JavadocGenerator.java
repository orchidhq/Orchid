package com.eden.orchid.javadoc;


import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.javadoc.pages.JavadocClassPage;
import com.eden.orchid.javadoc.pages.JavadocPackagePage;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public class JavadocGenerator extends OrchidGenerator {

    protected RootDoc rootDoc;

    public static List<JavadocClassPage> allClasses;
    public static List<JavadocPackagePage> allPackages;

    @Inject
    public JavadocGenerator(OrchidContext context, RootDoc rootDoc) {
        super(context, "javadoc", 800);
        this.rootDoc = rootDoc;
    }

    @Override
    public String getDescription() {
        return "Creates a page for each Class and Package in your project, displaying the expected Javadoc " +
                "information of methods, fields, etc. but in your site's theme.";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        Set<ClassDoc> classes = new HashSet<>();
        Set<PackageDoc> packages = new HashSet<>();

        for (ClassDoc classDoc : rootDoc.classes()) {
            classes.add(classDoc);
            packages.add(classDoc.containingPackage());
        }

        allClasses = new ArrayList<>();
        allPackages = new ArrayList<>();

        for (ClassDoc classDoc : classes) {
            allClasses.add(new JavadocClassPage(context, classDoc));
        }

        Map<PackageDoc, JavadocPackagePage> packagePageMap = new HashMap<>();
        for (PackageDoc packageDoc : packages) {
            List<JavadocClassPage> classesInPackage = new ArrayList<>();
            for (JavadocClassPage classDocPage : allClasses) {
                if(classDocPage.getClassDoc().containingPackage().equals(packageDoc)) {
                    classesInPackage.add(classDocPage);
                }
            }

            JavadocPackagePage packagePage = new JavadocPackagePage(context, packageDoc, classesInPackage);

            allPackages.add(packagePage);
            packagePageMap.put(packageDoc, packagePage);
        }

        for (JavadocClassPage classDocPage : allClasses) {
            classDocPage.setPackagePage(packagePageMap.get(classDocPage.getClassDoc().containingPackage()));
//            classDocPage.addComponent("summary", SummaryComponent.class);
//            classDocPage.addComponent("fields", FieldsComponent.class);
//            classDocPage.addComponent("ctors", ConstructorsComponent.class);
//            classDocPage.addComponent("methods", MethodsComponent.class);
        }

        List<OrchidPage> pages = new ArrayList<>();
        pages.addAll(allClasses);
        pages.addAll(allPackages);

        return pages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.forEach(context::renderTemplate);
    }
}