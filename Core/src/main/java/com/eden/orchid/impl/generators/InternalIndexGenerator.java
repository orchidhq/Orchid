package com.eden.orchid.impl.generators;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidReference;
import com.eden.orchid.api.resources.OrchidResource;
import com.eden.orchid.impl.docParser.docs.ClassDocParser;
import com.eden.orchid.impl.resources.JsonResource;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class InternalIndexGenerator extends OrchidGenerator {

    public JSONArray internalClasses;
    private List<OrchidPage> metaPages;
    private List<OrchidPage> pages;
    private JSONObject pagesIndex;
    private RootDoc root;

    private ClassDocParser classDocParser;

    @Inject
    public InternalIndexGenerator(ClassDocParser classDocParser) {
        this.classDocParser = classDocParser;
        this.priority = 800;
    }

    /**
     * # InternalIndexGenerator
     *
     * **generate pages for internal classes** _like a boss_
     */
    @Override
    public String getName() {
        return "internalClasses";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public JSONElement startIndexing() {
        root = getContext().getRootDoc();

        if(root == null) {
            return null;
        }

        internalClasses = new JSONArray();
        metaPages = new ArrayList<>();
        pages = new ArrayList<>();
        pagesIndex = new JSONObject();

        for(ClassDoc classDoc : root.classes()) {
            JSONObject classInfo = classDocParser.loadClassData(classDoc);

            OrchidPage classPage = new OrchidPage(new JsonResource(new JSONElement(classInfo), classDocParser.getReference(classDoc)));
            pages.add(classPage);

//            if(classInfo.has("info")) {
//                internalClasses.put(classInfo.getJSONObject("info"));
//            }
            internalClasses.put(classInfo);

            OrchidUtils.buildTaxonomy(classPage.getResource().getReference().getPath(), pagesIndex, classInfo.getJSONObject("info"));
        }

        OrchidResource classIndexResource = new JsonResource(new JSONElement(internalClasses), new OrchidReference("meta/classIndex.json"));
        OrchidPage classIndexPage = new OrchidPage(classIndexResource);
        JSONObject classIndexObject = new JSONObject();
        classIndexObject.put("name", classIndexPage.getReference().getTitle());
        classIndexObject.put("url", classIndexPage.getReference().toString());
        metaPages.add(classIndexPage);

//        OrchidResource classTreeResource = new JsonResource(new JSONElement(pagesIndex), new OrchidReference("meta/classDetails.json"));
//        OrchidPage classTreePage = new OrchidPage(classTreeResource);
//        JSONObject classTreeObject = new JSONObject();
//        classTreeObject.put("name", classTreePage.getReference().getTitle());
//        classTreeObject.put("url", classTreePage.getReference().toString());
//        metaPages.add(classTreePage);

        return new JSONElement(pagesIndex);
    }

    @Override
    public void startGeneration() {
        if(pages != null) {
            for (OrchidPage page : pages) {
                page.setAlias("classDoc");
                page.renderTemplate("templates/pages/classDoc.twig");
            }
        }
        if(metaPages != null) {
            for (OrchidPage metaPage : metaPages) {
                metaPage.renderRawContent();
            }
        }
    }

}
