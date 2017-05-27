package com.eden.orchid.javadoc.impl.generators;


import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.resource.JsonResource;
import com.eden.orchid.javadoc.JavadocClassPage;
import com.eden.orchid.javadoc.impl.docParsers.ClassDocParser;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class JavadocClassesGenerator extends OrchidGenerator {

    private RootDoc root;

    private ClassDocParser classDocParser;

    @Inject
    public JavadocClassesGenerator(OrchidContext context, ClassDocParser classDocParser) {
        super(context);
        this.classDocParser = classDocParser;
        this.priority = 800;
    }

    @Override
    public String getName() {
        return "javadoc";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        root = context.getRootDoc();

        if (root == null) {
            return null;
        }

        List<JavadocClassPage> pages = new ArrayList<>();

        for (ClassDoc classDoc : root.classes()) {
            JSONObject classInfo = classDocParser.loadClassData(classDoc);

            JavadocClassPage classPage = new JavadocClassPage(new JsonResource(new JSONElement(classInfo), classDocParser.getReference(classDoc)));
            classPage.getReference().setTitle(classDoc.typeName());

            classPage.setType("classDoc");
            pages.add(classPage);
        }

        return pages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.stream()
             .forEach((page -> page.renderTemplate()));
    }
}