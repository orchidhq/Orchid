package com.eden.orchid.javadoc;


import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Description;
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
import java.util.stream.Stream;

@Singleton
@Description("Creates a page for each Class and Package in your project, displaying the expected Javadoc information " +
        "of methods, fields, etc. but in your site's theme.")
public class JavadocGenerator extends OrchidGenerator {

    private final RootDoc rootDoc;

    private final JavadocModel model;

    @Inject
    public JavadocGenerator(OrchidContext context, RootDoc rootDoc, JavadocModel model) {
        super(context, "javadoc", 800);
        this.rootDoc = rootDoc;
        this.model = model;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        Set<ClassDoc> classes = new HashSet<>();
        Set<PackageDoc> packages = new HashSet<>();

        for (ClassDoc classDoc : rootDoc.classes()) {
            classes.add(classDoc);
            packages.add(classDoc.containingPackage());
        }

        model.initialize(new ArrayList<>(), new ArrayList<>());

        for (ClassDoc classDoc : classes) {
            model.getAllClasses().add(new JavadocClassPage(context, classDoc));
        }

        Map<PackageDoc, JavadocPackagePage> packagePageMap = new HashMap<>();
        for (PackageDoc packageDoc : packages) {
            List<JavadocClassPage> classesInPackage = new ArrayList<>();
            for (JavadocClassPage classDocPage : model.getAllClasses()) {
                if(classDocPage.getClassDoc().containingPackage().equals(packageDoc)) {
                    classesInPackage.add(classDocPage);
                }
            }

            JavadocPackagePage packagePage = new JavadocPackagePage(context, packageDoc, classesInPackage);

            model.getAllPackages().add(packagePage);
            packagePageMap.put(packageDoc, packagePage);
        }

        for (JavadocClassPage classDocPage : model.getAllClasses()) {
            classDocPage.setPackagePage(packagePageMap.get(classDocPage.getClassDoc().containingPackage()));
        }

        return model.getAllPages();
    }

    @Override
    public void startGeneration(Stream<? extends OrchidPage> pages) {
        pages.forEach(context::renderTemplate);
    }
}