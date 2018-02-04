package com.eden.orchid.javadoc;


import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidCollection;
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
import java.util.*;
import java.util.stream.Stream;

@Singleton
@Description("Creates a page for each Class and Package in your project, displaying the expected Javadoc information " +
        "of methods, fields, etc. but in your site's theme.")
public class JavadocGenerator extends OrchidGenerator {

    private final RootDoc rootDoc;

    private final JavadocModel model;

    @Inject
    public JavadocGenerator(OrchidContext context, RootDoc rootDoc, JavadocModel model) {
        super(context, "javadoc", OrchidGenerator.PRIORITY_EARLY);
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

            classesInPackage.sort(Comparator.comparing(JavadocClassPage::getTitle));

            JavadocPackagePage packagePage = new JavadocPackagePage(context, packageDoc, classesInPackage);

            model.getAllPackages().add(packagePage);
            packagePageMap.put(packageDoc, packagePage);
        }

        for(JavadocPackagePage packagePage : packagePageMap.values()) {
            for(JavadocPackagePage possibleInnerPackage : packagePageMap.values()) {
                if(isInnerPackage(packagePage.getPackageDoc(), possibleInnerPackage.getPackageDoc())) {
                    packagePage.getInnerPackages().add(possibleInnerPackage);
                }
            }
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

    @Override
    public List<? extends OrchidCollection> getCollections() {
        return null;
    }


    private boolean isInnerPackage(PackageDoc parent, PackageDoc possibleChild) {

        // packages start the same...
        if(possibleChild.name().startsWith(parent.name())) {

            // packages are not the same...
            if(!possibleChild.name().equals(parent.name())) {

                int parentSegmentCount = parent.name().split("\\.").length;
                int possibleChildSegmentCount = possibleChild.name().split("\\.").length;

                // child has one segment more than the parent, so is immediate child
                if(possibleChildSegmentCount == (parentSegmentCount+1)) {
                    return true;
                }
            }
        }

        return false;
    }
}