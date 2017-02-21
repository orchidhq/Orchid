package com.eden.orchid.impl.generators;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.resource.JsonResource;
import com.eden.orchid.impl.docParser.docs.ClassDocParser;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ClassesGenerator extends OrchidGenerator {

    private RootDoc root;

    private ClassDocParser classDocParser;

    @Inject
    public ClassesGenerator(OrchidContext context, ClassDocParser classDocParser) {
        super(context);
        this.classDocParser = classDocParser;
        this.priority = 800;
    }

    /**
     * # ClassesGenerator
     *
     * **generate pages for internal classes** _like a boss_
     */
    @Override
    public String getName() {
        return "classes";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<OrchidPage> startIndexing() {
        root = context.getRootDoc();

        if(root == null) {
            return null;
        }

        List<OrchidPage> pages = new ArrayList<>();

        for(ClassDoc classDoc : root.classes()) {
            JSONObject classInfo = classDocParser.loadClassData(classDoc);

            OrchidPage classPage = new OrchidPage(new JsonResource(new JSONElement(classInfo), classDocParser.getReference(classDoc)));
            classPage.setData(classInfo);
            classPage.getReference().setTitle(classDoc.simpleTypeName());

            pages.add(classPage);
        }

        return pages;
    }

    @Override
    public void startGeneration(List<OrchidPage> pages) {
        pages.stream()
             .forEach((page -> {
                 page.setAlias("classDoc");
                 page.renderTemplate("templates/pages/classDoc.twig");
             }));
    }

}
